package is.rares.kumo.controller;

import io.swagger.annotations.ApiOperation;
import is.rares.kumo.model.requests.LoginRequest;
import is.rares.kumo.model.requests.RegisterRequest;
import is.rares.kumo.model.responses.SuccessResponse;
import is.rares.kumo.model.responses.TokenDataResponse;
import is.rares.kumo.security.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.Nullable;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(path = "/api/authenticate")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(value = "Login", response = SuccessResponse.class)
    @PostMapping(value = "/login")
    public TokenDataResponse login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @ApiOperation(value = "Register", response = SuccessResponse.class)
    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request, @Nullable @RequestHeader("Register-Invite") String registerInvite) {
        return authenticationService.register(request, registerInvite);
    }
}
