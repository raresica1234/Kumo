package is.rares.kumo.service.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import is.rares.kumo.core.config.KumoConfig;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.FileErrorCodes;
import is.rares.kumo.domain.content.ThumbnailSizeEnum;
import is.rares.kumo.security.domain.CurrentUser;
import is.rares.kumo.service.explore.ExplorerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ImageService {
    private final ExplorerService explorerService;
    private final ThumbnailService thumbnailService;
    private final KumoConfig kumoConfig;

    public InputStreamResource getImage(String path, int width, boolean original, CurrentUser currentUser) {
        if (path.isEmpty())
            throw new KumoException(FileErrorCodes.NOT_FOUND);

        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
        log.info("User {} requested image:{}: {}", currentUser.getUsername(), width, decodedPath);

        String realPath = Paths.get(kumoConfig.getMediaPath(), decodedPath)
            .toAbsolutePath().normalize().toString();

        if (!explorerService.canAccessPath(realPath, currentUser))
            throw new KumoException(FileErrorCodes.NOT_FOUND);


        var file = new File(realPath);

        if (!file.exists() || !file.isFile())
            throw new KumoException(FileErrorCodes.NOT_FOUND);


        var thumbnailSize = ThumbnailSizeEnum.getThumbnailSize(width);

        if (thumbnailSize == ThumbnailSizeEnum.ORIGINAL) {
            if (original)
                return getOriginalImage(realPath);
            else
                return thumbnailService.getOptimizedImage(realPath);
        }

        return thumbnailService.getThumbnail(realPath, thumbnailSize);
    }


    private InputStreamResource getOriginalImage(String path) {
        File file = new File(path);

        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            return new InputStreamResource(inputStream);
        } catch (FileNotFoundException e) {
            throw new KumoException(FileErrorCodes.NOT_FOUND);
        }
    }
}
