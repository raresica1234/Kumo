package is.rares.kumo.controller;

import is.rares.kumo.security.enums.Feature;
import is.rares.kumo.model.UserModel;
import is.rares.kumo.security.services.CurrentUserService;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    @Authenticated
    @HasTokenType
    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public UserModel getUser() {
        return userService.getUser(currentUserService.getUser());
    }

    @Authenticated
    @HasTokenType
    @GetMapping(path = "features", produces = MediaType.APPLICATION_JSON)
    public Set<Feature> getFeatures() {
        return userService.getFeatures(currentUserService.getUser());
    }

}
