package is.rares.kumo.service.content;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.FileErrorCodes;
import is.rares.kumo.security.domain.CurrentUser;
import is.rares.kumo.service.explore.ExplorerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.imgscalr.Scalr;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

@Service
@AllArgsConstructor
@Slf4j
public class ThumbnailService {
    private final ExplorerService explorerService;

    public InputStreamResource getThumbnail(String path, CurrentUser currentUser) {
        if (path.isEmpty())
            throw new KumoException(FileErrorCodes.NOT_FOUND);

        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
        log.info("User {} requested file: {}", currentUser.getUsername(), decodedPath);

        if (!explorerService.canAccessPath(decodedPath, currentUser))
            throw new KumoException(FileErrorCodes.NOT_FOUND);

        var file = new File(decodedPath);

        if (!file.exists() || !file.isFile())
            throw new KumoException(FileErrorCodes.NOT_FOUND);


        return createSmallThumbnail(decodedPath);
    }

    private InputStreamResource createSmallThumbnail(String path) {
        File imageFile = new File(path);
        try {
			BufferedImage originalImage = ImageIO.read(imageFile);

            BufferedImage scaledImage = Scalr.resize(originalImage, Scalr.Method.BALANCED, 200);

            String[] split = path.split("\\.");

            String extension = split[split.length - 1];

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, extension, outputStream);

            return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
		} catch (IOException e) {
            log.error("Could not open file {}", path);
            throw new KumoException(FileErrorCodes.NOT_FOUND);
		}
    }
}
