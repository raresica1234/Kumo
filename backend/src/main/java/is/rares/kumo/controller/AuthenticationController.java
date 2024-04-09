package is.rares.kumo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.rares.kumo.controller.requests.user.AccountCodeRequest;
import is.rares.kumo.controller.requests.user.CreateRegisterInviteRequest;
import is.rares.kumo.controller.requests.user.LoginRequest;
import is.rares.kumo.controller.requests.user.RegisterRequest;
import is.rares.kumo.controller.responses.BooleanResponse;
import is.rares.kumo.controller.responses.user.RegisterInviteResponse;
import is.rares.kumo.controller.responses.user.TokenDataResponse;
import is.rares.kumo.domain.user.Feature;
import is.rares.kumo.security.services.CurrentUserService;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasAuthority;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.annotation.NotAuthenticated;
import is.rares.kumo.security.enums.TokenType;
import is.rares.kumo.security.model.LoggedClientModel;
import is.rares.kumo.security.services.AuthenticationService;
import is.rares.kumo.service.RegisterInviteService;
import is.rares.kumo.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static is.rares.kumo.security.AuthorizationInterceptor.BEARER_ATTRIBUTE;

@RestController
@Slf4j
@RequestMapping(path = "/api/authenticate")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final RegisterInviteService registerInviteService;

    public AuthenticationController(AuthenticationService authenticationService,
                                    UserService userService,
                                    CurrentUserService currentUserService,
                                    RegisterInviteService registerInviteService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.currentUserService = currentUserService;
        this.registerInviteService = registerInviteService;
    }

    @Operation(summary = "Login", operationId = "login", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDataResponse.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect password"),
            @ApiResponse(responseCode = "404", description = "Username not found")
    })
    @NotAuthenticated
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public TokenDataResponse login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @Operation(summary = "Register", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invitation invalid"),
            @ApiResponse(responseCode = "400", description = "Duplicate username"),
            @ApiResponse(responseCode = "400", description = "Duplicate email"),
    })
    @NotAuthenticated
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request,
                                           @Nullable @RequestHeader("Register-Invite") String registerInvite) {
        return userService.register(request, registerInvite);
    }

    @Operation(summary = "Is Register by invites required", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BooleanResponse.class))),
    })
    @NotAuthenticated
    @GetMapping(value = "/requireRegisterInvite", produces = MediaType.APPLICATION_JSON)
    public BooleanResponse registerByInvites() {
        return userService.registerInviteRequired();
    }

    @Operation(summary = "Create register invite", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Code not found"),
            @ApiResponse(responseCode = "422", description = "Code expired"),
            @ApiResponse(responseCode = "422", description = "Code already used"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.CREATE_REGISTER_INVITE)
    @PostMapping(value = "/createRegisterInvite", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public RegisterInviteResponse createRegisterInvite(@Parameter(name = "Register invite request", required = true)
                                          @Valid @RequestBody CreateRegisterInviteRequest registerInviteRequest) {
        return registerInviteService.createRegisterInvite(registerInviteRequest);
    }

    @Operation(summary = "Validate 2FA code", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDataResponse.class))),
            @ApiResponse(responseCode = "404", description = "Code not found"),
            @ApiResponse(responseCode = "422", description = "Code expired"),
            @ApiResponse(responseCode = "422", description = "Code already used"),
    })
    @Authenticated
    @HasTokenType(TokenType.TWO_FA_TOKEN)
    @PostMapping(value = "/validate2FA", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public TokenDataResponse validateCode(@Parameter(name = "Account code request", required = true)
                                          @Valid @RequestBody AccountCodeRequest request) {
        return authenticationService.validateTwoFactorCode(request, currentUserService.getUser());
    }

    @Operation(summary = "Check in 2FA is required", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BooleanResponse.class))),
    })
    @Authenticated
    @HasTokenType(TokenType.ANY)
    @GetMapping(value = "/require2FA", produces = MediaType.APPLICATION_JSON)
    public BooleanResponse require2FA() {
        return authenticationService.isTwoFARequired(currentUserService.getUser());
    }

    @Operation(summary = "Refresh token", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDataResponse.class))),
    })
    @NotAuthenticated
    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON)
    public TokenDataResponse refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return this.authenticationService.refreshToken(refreshToken.substring(BEARER_ATTRIBUTE.length()));
    }

    @Operation(summary = "List Client Locations", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = List.class))),
    })
    @Authenticated
    @HasTokenType
    @GetMapping(value = "/clients", produces = MediaType.APPLICATION_JSON)
    public List<LoggedClientModel> getAllLoggedClients() {
        return this.authenticationService.getLoggedClients(currentUserService.getUser());
    }
}
