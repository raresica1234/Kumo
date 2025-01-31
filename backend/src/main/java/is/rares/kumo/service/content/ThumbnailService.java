package is.rares.kumo.service.content;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.stereotype.Service;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.FileErrorCodes;
import is.rares.kumo.domain.content.Thumbnail;
import is.rares.kumo.domain.content.ThumbnailSizeEnum;
import is.rares.kumo.utils.FileUtils;
import is.rares.kumo.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ThumbnailService {

    // Key definition: thumbnail::sha1(filename)::size::sha1(content)
    private static final String THUMBNAIL_DOCUMENT_NAME = "thumbnail::";

    private final RedisTemplate<String, Thumbnail> redisTemplate;
    private final TaskExecutor taskExecutor;

    @Value("${kumo.thumbnailDirectory:data}")
    private String thumbnailDirectoryPath;

    public InputStreamResource getThumbnail(String path, ThumbnailSizeEnum thumbnailSize) {
        var thumbnail = getRedisThumbnail(path, thumbnailSize);

        if (thumbnail == null) {
            log.debug("Thumbnail {} not found in redis, creating on demand", path);
            return createThumbnailOnDemand(path, thumbnailSize);
        }

        return getThumbnailFromFile(thumbnail);
    }

    private InputStreamResource createThumbnailOnDemand(String path, ThumbnailSizeEnum thumbnailSize) {
        File imageFile = new File(path);
        try {
			BufferedImage originalImage = ImageIO.read(imageFile);

            BufferedImage scaledImage = Scalr.resize(originalImage, Scalr.Method.BALANCED, thumbnailSize.maxSize);

            String[] split = path.split("\\.");

            String extension = split[split.length - 1];

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, extension, outputStream);

            CompletableFuture.runAsync(() -> cacheThumbnail(path, thumbnailSize), taskExecutor)
                .exceptionally((e) -> {
                    log.error("Could not create thumbnail of size {} for {}", thumbnailSize.maxSize, path, e);
                    return null;
            });

            return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
		} catch (IOException e) {
            log.error("Could not open file {}", path);
            throw new KumoException(FileErrorCodes.NOT_FOUND);
		}
    }


    private void cacheThumbnail(String path, ThumbnailSizeEnum thumbnailSize) {
        String contentHash;
        try {
            contentHash = HashUtils.hashFileContent(path);
        } catch (Exception e) {
            log.error("Could not create content hash for file {}", path, e);
            return;
        }

        Set<String> keys = redisTemplate.keys(THUMBNAIL_DOCUMENT_NAME + "*::" + thumbnailSize.maxSize + "::" + contentHash);

        if (!keys.isEmpty()) 
            if (useExistingThumbnail(keys.stream().findFirst().get(), path, thumbnailSize))
                return;

        createAndSaveThumbnail(path, thumbnailSize, contentHash);

    }

    private void createAndSaveThumbnail(String path, ThumbnailSizeEnum size, String contentHash) {
        if (!FileUtils.createDirectories(thumbnailDirectoryPath)) {
            log.error("Thumbnail directory doesn't exist or invalid");
            return;
        }

  
        String folderPath = thumbnailDirectoryPath + "/" + contentHash.substring(0, 2) + "/" + contentHash.substring(2, 4);

        if (!FileUtils.createDirectories(folderPath))
            throw new RuntimeException(String.format("Could not create directory for thumbnail %s", folderPath));
    

        try {
            File imageFile = new File(path);
            File outputFile = new File(folderPath + "/" + contentHash);
			BufferedImage originalImage = ImageIO.read(imageFile);

            BufferedImage scaledImage = Scalr.resize(originalImage, Scalr.Method.BALANCED, size.maxSize);

            String[] split = path.split("\\.");

            String extension = split[split.length - 1];

            ImageIO.write(scaledImage, extension, outputFile);

            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setPath(path);
            thumbnail.setContentHash(contentHash);
            thumbnail.setLastModifiedTimestamp(imageFile.lastModified());
            thumbnail.setSize(size.maxSize);

            String redisKey = THUMBNAIL_DOCUMENT_NAME + HashUtils.hashString(path) + "::" + size.maxSize + "::" + contentHash;

            redisTemplate.opsForValue().set(redisKey, thumbnail);
            log.debug("Saved thumbnail for {}:{} in redis", path, size.maxSize);
		} catch (IOException e) {
            log.error("Could not open file {}", path);
            throw new KumoException(FileErrorCodes.NOT_FOUND);
		}

    }

    private boolean useExistingThumbnail(String key, String path, ThumbnailSizeEnum thumbnailSize) {
        Thumbnail thumbnail = redisTemplate.opsForValue().get(key);
        if (thumbnail == null) {
            log.error("Using existing thumbnail for {}:{} failed, thumbnail not found in redis", path, thumbnailSize.maxSize);
            return false;
        }

        thumbnail.setPath(path);

        redisTemplate.opsForValue().set(key, thumbnail);

        return true;
    }

    private InputStreamResource getThumbnailFromFile(Thumbnail thumbnail) {
        String path = computePathForContentHash(thumbnail.getContentHash());

        File file = new File(path);

        FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
            log.debug("Thumbnail found in redis");
            return new InputStreamResource(fileInputStream);
		} catch (FileNotFoundException e) {
            log.error("Thumbnail for path {} with content hash {} not found in thumbnail directory. Generating on the fly...", thumbnail.getPath(), thumbnail.getContentHash(), e);
            return createThumbnailOnDemand(thumbnail.getPath(), ThumbnailSizeEnum.getThumbnailSize(thumbnail.getSize()));
        }
    }


    private String computePathForContentHash(String contentHash) {
        String folderPath = thumbnailDirectoryPath + "/" + contentHash.substring(0, 2) + "/" + contentHash.substring(2, 4);

        return folderPath + "/" + contentHash;
    }


    private Thumbnail getRedisThumbnail(String path, ThumbnailSizeEnum thumbnailSize) {
        var hashedPath = DigestUtils.sha1DigestAsHex(path);
        Set<String> keys = redisTemplate.keys(THUMBNAIL_DOCUMENT_NAME + hashedPath + "::" + thumbnailSize.maxSize + "::*");

        var results = keys.stream()
            .map(key -> redisTemplate.opsForValue().get(key))
            .toList(); 

        for (var result : results) {
            if (checkIfThumbnailIsValid(path, result))
                return result;
        }

        return null;
    }


    private boolean checkIfThumbnailIsValid(String path, Thumbnail thumbnail) {
        var file = new File(path);
        return file.lastModified() == thumbnail.getLastModifiedTimestamp();
    }
}
