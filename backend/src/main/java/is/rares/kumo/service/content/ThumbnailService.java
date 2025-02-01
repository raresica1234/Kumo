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

        return getThumbnailFromFile(thumbnail, thumbnailSize, false);
    }


    public InputStreamResource getOptimizedImage(String path) {
        ThumbnailSizeEnum thumbnailSize = ThumbnailSizeEnum.ORIGINAL;
        var thumbnail = getRedisThumbnail(path, thumbnailSize);


        if (thumbnail == null) {
            log.debug("Thumbnail {} not found in redis, creating on demand", path);
            return createOptimizedImage(path, thumbnailSize);
        }

        return getThumbnailFromFile(thumbnail, thumbnailSize, true);
    }

    private InputStreamResource createOptimizedImage(String path, ThumbnailSizeEnum thumbnailSize) {
        File imageFile = new File(path);
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);

            String[] split = path.split("\\.");

            String extension = split[split.length - 1];

            BufferedImage scaledImage = Scalr.resize(originalImage, Scalr.Method.SPEED, originalImage.getWidth(), originalImage.getHeight());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, extension, outputStream);

            CompletableFuture.runAsync(() -> cacheThumbnail(scaledImage, path, thumbnailSize), taskExecutor)
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

    private InputStreamResource createThumbnailOnDemand(String path, ThumbnailSizeEnum thumbnailSize) {
        File imageFile = new File(path);
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);

            String[] split = path.split("\\.");

            String extension = split[split.length - 1];

            if (originalImage.getWidth() <= thumbnailSize.maxSize || originalImage.getHeight() <= thumbnailSize.maxSize) {
                log.debug("Image is smaller than thumbnail requested, serving image");
                return handleImageSmallerThanThumbnail(path, originalImage, extension, thumbnailSize);

            }

            BufferedImage scaledImage = Scalr.resize(originalImage, Scalr.Method.BALANCED, thumbnailSize.maxSize);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, extension, outputStream);

            CompletableFuture.runAsync(() -> cacheThumbnail(scaledImage, path, thumbnailSize), taskExecutor)
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

    private InputStreamResource handleImageSmallerThanThumbnail(String path, BufferedImage originalImage, String extension, ThumbnailSizeEnum thumbnailSize) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(originalImage, extension, outputStream);

        CompletableFuture.runAsync(() -> storeOriginalImageAsThumbnail(path, thumbnailSize), taskExecutor)
            .exceptionally((e) -> {
                log.error("Could not store original image as thumbnail for size {} for {}", thumbnailSize.maxSize, path, e);
                return null;
            });


        return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
    }


    private void storeOriginalImageAsThumbnail(String path, ThumbnailSizeEnum thumbnailSize) {
        String contentHash;
        try {
            contentHash = HashUtils.hashFileContent(path);
        } catch (Exception e) {
            log.error("Could not create content hash for file {}", path, e);
            return;
        }

        File image = new File(path);

        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setContentHash(contentHash);
        thumbnail.setSize(thumbnailSize.maxSize);
        thumbnail.setPath(path);
        thumbnail.setOriginalImage(true);
        thumbnail.setLastModifiedTimestamp(image.lastModified());

        String redisKey = getRedisKey(path, thumbnailSize.maxSize, contentHash);

        redisTemplate.opsForValue().set(redisKey, thumbnail);
    }

    private void cacheThumbnail(BufferedImage scaledImage, String path, ThumbnailSizeEnum thumbnailSize) {
        String contentHash;
        try {
            contentHash = HashUtils.hashFileContent(path);
        } catch (Exception e) {
            log.error("Could not create content hash for file {}", path, e);
            return;
        }

        File thumbnailFile = new File(computePathForContentHash(contentHash, thumbnailSize));
        if (!thumbnailFile.exists()) {
            log.debug("Thumbnail is missing, recreating {}:{}", path, thumbnailSize.maxSize);
            createAndSaveThumbnail(scaledImage, path, thumbnailSize, contentHash);
            return;
        }

        Set<String> keys = redisTemplate.keys(THUMBNAIL_DOCUMENT_NAME + "*::" + thumbnailSize.maxSize + "::" + contentHash);

        File imageFile = new File(path); 

        if (!keys.isEmpty()) 
            if (useExistingThumbnail(keys.stream().findFirst().get(), path, thumbnailSize, imageFile.lastModified()))
                return;

        createAndSaveThumbnail(scaledImage, path, thumbnailSize, contentHash);

    }

    private void createAndSaveThumbnail(BufferedImage scaledImage, String path, ThumbnailSizeEnum size, String contentHash) {
        String basePath = getBasePathForThumbnail(size);

        if (!FileUtils.createDirectories(basePath)) {
            log.error("Thumbnail directory doesn't exist or invalid");
            return;
        }


        String folderPath = basePath + "/" + contentHash.substring(0, 2) + "/" + contentHash.substring(2, 4);

        if (!FileUtils.createDirectories(folderPath))
            throw new RuntimeException(String.format("Could not create directory for thumbnail %s", folderPath));

        try {
            File imageFile = new File(path);
            File outputFile = new File(folderPath + "/" + contentHash);

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

    private boolean useExistingThumbnail(String key, String path, ThumbnailSizeEnum thumbnailSize, long lastModified) {
        Thumbnail thumbnail = redisTemplate.opsForValue().get(key);
        if (thumbnail == null) {
            log.error("Using existing thumbnail for {}:{} failed, thumbnail not found in redis", path, thumbnailSize.maxSize);
            return false;
        }

        thumbnail.setPath(path);
        thumbnail.setLastModifiedTimestamp(lastModified);

        redisTemplate.opsForValue().set(getRedisKey(HashUtils.hashString(path), thumbnail.getSize(), thumbnail.getContentHash()), thumbnail);

        log.debug("Thumbnail already exists for {}", path);
        return true;
    }

    private InputStreamResource getThumbnailFromFile(Thumbnail thumbnail, ThumbnailSizeEnum size, boolean optimizedImage) {
        String path;
        if (thumbnail.isOriginalImage()) {
            path = thumbnail.getPath();
            log.debug("Using original image as thumbnail");
        }
        else
            path = computePathForContentHash(thumbnail.getContentHash(), size);

        File file = new File(path);

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            log.debug("Thumbnail found in redis {}", thumbnail.getPath());
            return new InputStreamResource(fileInputStream);
        } catch (FileNotFoundException e) {
            log.error("Thumbnail for path {} with content hash {} not found in thumbnail directory. Generating on the fly...", thumbnail.getPath(), thumbnail.getContentHash());
            if (optimizedImage)
                return createOptimizedImage(thumbnail.getPath(), size);
            else
                return createThumbnailOnDemand(thumbnail.getPath(), size);
        }
    }


    private String computePathForContentHash(String contentHash, ThumbnailSizeEnum size) {
        String folderPath = getBasePathForThumbnail(size) + "/" + contentHash.substring(0, 2) + "/" + contentHash.substring(2, 4);

        return folderPath + "/" + contentHash;
    }


    private Thumbnail getRedisThumbnail(String path, ThumbnailSizeEnum thumbnailSize) {
        var hashedPath = HashUtils.hashString(path);
        var keyString = THUMBNAIL_DOCUMENT_NAME + hashedPath + "::" + thumbnailSize.maxSize + "*";
        Set<String> keys = redisTemplate.keys(keyString);

        var results = keys.stream()
            .map(key -> redisTemplate.opsForValue().get(key))
            .toList(); 

        if (results.isEmpty()) {
            log.debug("Thumbnail not found in redis");
            return null;
        }

        for (var result : results) {
            if (checkIfThumbnailIsValid(path, result))
                return result;
        }

        log.debug("Last modified timestamp changed for {}", path);

        return null;
    }


    private boolean checkIfThumbnailIsValid(String path, Thumbnail thumbnail) {
        var file = new File(path);
        return file.exists() && file.lastModified() == thumbnail.getLastModifiedTimestamp();
    }


    private String getRedisKey(String path, int width, String contentHash) {
        return THUMBNAIL_DOCUMENT_NAME + path + "::" + width + "::" + contentHash;
    }


    private String getBasePathForThumbnail(ThumbnailSizeEnum thumbnailSize) {
        if (thumbnailSize == ThumbnailSizeEnum.ORIGINAL)
            return thumbnailDirectoryPath + "/original";
        else
            return thumbnailDirectoryPath + "/" + thumbnailSize.maxSize;
    }
}
