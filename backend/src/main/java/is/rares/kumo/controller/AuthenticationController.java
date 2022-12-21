package is.rares.kumo.controller;

import io.swagger.annotations.ApiOperation;
import is.rares.kumo.model.requests.LoginRequest;
import is.rares.kumo.model.responses.SuccessResponse;
import is.rares.kumo.model.responses.TokenDataResponse;
import is.rares.kumo.security.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/authenticate")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(value = "Login", response = SuccessResponse.class)
    @PostMapping(value = "/login")
    public TokenDataResponse login(@RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }
}
