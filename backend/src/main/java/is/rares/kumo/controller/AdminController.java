package is.rares.kumo.controller;

import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.rares.kumo.controller.responses.BooleanResponse;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasAuthority;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.enums.Feature;
import is.rares.kumo.service.AdminService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/api/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.OWNER)
    @PostMapping(path = "/thumbnailGarbageCollection", produces = MediaType.APPLICATION_JSON)
    public BooleanResponse runThumbnailGarbageCollection() {
        adminService.runThumbnailGarbageCollection();
        return new BooleanResponse(true);
    }
}
