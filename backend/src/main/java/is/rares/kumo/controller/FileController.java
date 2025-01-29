package is.rares.kumo.controller;

import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.services.CurrentUserService;
import is.rares.kumo.service.content.FileService;
import is.rares.kumo.service.content.ThumbnailService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping(path = "/api/file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;
    private final ThumbnailService thumbnailService;
    private final CurrentUserService currentUserService;

    @Authenticated
    @HasTokenType
    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM)
    public InputStreamResource getFile(@RequestParam("path") String path) {
        var currentUser = currentUserService.getUser();
        return fileService.getFile(path, currentUser);
    }

    @Authenticated
    @HasTokenType
    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM, path = "/thumbnail")
    public InputStreamResource getThumbnail(@RequestParam("path") String path) {
        var currentUser = currentUserService.getUser();
        return thumbnailService.getThumbnail(path, currentUser);
    }
}
