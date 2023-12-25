package is.rares.kumo.controller;

import is.rares.kumo.model.UserModel;
import is.rares.kumo.security.CurrentUserService;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    public UserController(UserService userService, CurrentUserService currentUserService) {
        this.userService = userService;
        this.currentUserService = currentUserService;
    }


    @Authenticated
    @HasTokenType
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON)
    public UserModel getUser() {
        return userService.getUser(currentUserService.getUser());
    }

}
