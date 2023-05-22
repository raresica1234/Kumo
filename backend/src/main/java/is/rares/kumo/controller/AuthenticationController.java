package is.rares.kumo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.rares.kumo.controller.requests.AccountCodeRequest;
import is.rares.kumo.controller.requests.LoginRequest;
import is.rares.kumo.controller.requests.RegisterRequest;
import is.rares.kumo.controller.responses.SuccessResponse;
import is.rares.kumo.controller.responses.TokenDataResponse;
import is.rares.kumo.model.authentication.LoggedClientModel;
import is.rares.kumo.security.CurrentUserService;
import is.rares.kumo.security.services.AuthenticationService;
import is.rares.kumo.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static is.rares.kumo.security.AuthorizationInterceptor.BEARER_ATTRIBUTE;

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

    @Operation(summary = "Login", operationId = "login", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDataResponse.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect password"),
            @ApiResponse(responseCode = "404", description = "Username not found")
    })
    @PostMapping(value = "/login")
    public TokenDataResponse login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @Operation(summary = "Register", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invitation invalid"),
            @ApiResponse(responseCode = "400", description = "Duplicate username"),
            @ApiResponse(responseCode = "400", description = "Duplicate email"),

    })
    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request,
                                           @Nullable @RequestHeader("Register-Invite") String registerInvite) {
        return userService.register(request, registerInvite);
    }

    @Operation(summary = "Validate 2FA code", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDataResponse.class))),
            @ApiResponse(responseCode = "404", description = "Code not found"),
            @ApiResponse(responseCode = "422", description = "Code expired"),
            @ApiResponse(responseCode = "422", description = "Code already used"),
    })
    @PostMapping(value = "/validate2FA")
    public TokenDataResponse validateCode(@Parameter(name = "Account code request", required = true)
                                          @Valid @RequestBody AccountCodeRequest request) {
        return authenticationService.validateTwoFactorCode(request, currentUserService.getUser());
    }

    @Operation(summary = "Refresh token", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDataResponse.class))),
    })
    @PostMapping(value = "/refresh-token")
    public TokenDataResponse refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return this.authenticationService.refreshToken(currentUserService.getUser(), refreshToken.substring(BEARER_ATTRIBUTE.length()));
    }

    @Operation(summary = "List Client Locations", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = List.class))),
    })
    @GetMapping(value = "/clients")
    public List<LoggedClientModel> getAllLoggedClients() {
        return this.authenticationService.getLoggedClients(currentUserService.getUser());
    }
}
