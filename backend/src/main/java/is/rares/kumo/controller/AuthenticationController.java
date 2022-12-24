package is.rares.kumo.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import is.rares.kumo.model.requests.AccountCodeRequest;
import is.rares.kumo.model.requests.LoginRequest;
import is.rares.kumo.model.requests.RegisterRequest;
import is.rares.kumo.model.responses.SuccessResponse;
import is.rares.kumo.model.responses.TokenDataResponse;
import is.rares.kumo.security.CurrentUserService;
import is.rares.kumo.security.services.AuthenticationService;
import is.rares.kumo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(path = "/api/authenticate")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    private final CurrentUserService currentUserService;

    public AuthenticationController(AuthenticationService authenticationService,
                                    UserService userService,
                                    CurrentUserService currentUserService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @ApiOperation(value = "Login", response = SuccessResponse.class)
    @PostMapping(value = "/login")
    public TokenDataResponse login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @ApiOperation(value = "Register", response = SuccessResponse.class)
    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request, @Nullable @RequestHeader("Register-Invite") String registerInvite) {
        return userService.register(request, registerInvite);
    }

    @ApiOperation(value = "Validate 2FA code", response = SuccessResponse.class)
    @PostMapping(value = "/validate2FA")
    public ResponseEntity<TokenDataResponse> validateCode(@ApiParam(value = "Account code request", required = true)
                                                          @Valid @RequestBody AccountCodeRequest request) {
        return new ResponseEntity<>(authenticationService.validateTwoFactorCode(request, currentUserService.getUser()), HttpStatus.OK);
    }
}
