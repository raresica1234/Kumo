package is.rares.kumo.security.services;

import is.rares.kumo.controller.requests.user.AccountCodeRequest;
import is.rares.kumo.controller.requests.user.LoginRequest;
import is.rares.kumo.controller.responses.BooleanResponse;
import is.rares.kumo.controller.responses.user.TokenDataResponse;
import is.rares.kumo.core.config.JWTConfiguration;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import is.rares.kumo.core.exceptions.codes.AuthorizationErrorCodes;
import is.rares.kumo.domain.user.User;
import is.rares.kumo.repository.user.UserRepository;
import is.rares.kumo.security.AuthorizationInterceptor;
import is.rares.kumo.security.mapping.ClientLocationMapping;
import is.rares.kumo.security.domain.ClientLocation;
import is.rares.kumo.security.domain.CurrentUser;
import is.rares.kumo.security.enums.TokenType;
import is.rares.kumo.security.model.ClientLocationModel;
import is.rares.kumo.security.model.LoggedClientModel;
import is.rares.kumo.security.token.AsyncTokenStore;
import is.rares.kumo.security.token.Token;
import is.rares.kumo.security.token.TokenRepository;
import is.rares.kumo.security.token.TokenStore;
import is.rares.kumo.service.AccountCodesService;
import is.rares.kumo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AuthenticationService {
    private final JwtService jwtService;
    private final JWTConfiguration jwtConfiguration;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final TokenStore tokenStore;
    private final AsyncTokenStore asyncTokenStore;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AccountCodesService accountCodesService;

    private final ClientLocationMapping clientLocationMapping;

    @Autowired
    public AuthenticationService(JwtService jwtService, JWTConfiguration jwtConfiguration,
                                 UserRepository userRepository,
                                 TokenStore tokenStore,
                                 AsyncTokenStore asyncTokenStore,
                                 UserService userService,
                                 PasswordEncoder passwordEncoder,
                                 AccountCodesService accountCodesService,
                                 ClientLocationMapping clientLocationMapping,
                                 TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.jwtConfiguration = jwtConfiguration;
        this.userRepository = userRepository;
        this.tokenStore = tokenStore;
        this.asyncTokenStore = asyncTokenStore;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.accountCodesService = accountCodesService;
        this.clientLocationMapping = clientLocationMapping;
        this.tokenRepository = tokenRepository;
    }

    public TokenDataResponse login(LoginRequest request) {
        final User user = this.userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new KumoException(AccountCodeErrorCodes.USERNAME_NOT_FOUND, "Username or Email not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new KumoException(AccountCodeErrorCodes.PASSWORD_INCORRECT, "Password incorrect");

        return this.generateTokenDataResponse(user, request.getClientLocation(), true);
    }

    public TokenDataResponse generateTokenDataResponse(User user, ClientLocationModel clientLocationModel, boolean isFirstLogin) {
        TokenType tokenType = user.isUsing2FA() && isFirstLogin ? TokenType.TWO_FA_TOKEN : TokenType.NORMAL_TOKEN;

        final TokenDataResponse tokenDataResponse = TokenDataResponse.builder()
                .jwtToken(jwtService.generateAccessToken(user.getUuid(), tokenType))
                .refreshToken(jwtService.generateRefreshToken(user.getUuid()))
                .validityMs(jwtConfiguration.getAccessTokenValidity())
                .build();

        if (user.isUsing2FA() && isFirstLogin)
            accountCodesService.generateTwoFactorCode(user);

        ClientLocation clientLocation = clientLocationMapping.mapModelToEntity(clientLocationModel);

        this.asyncTokenStore.saveUserToken(tokenDataResponse, user.getUuid(), clientLocation, tokenType);
        return tokenDataResponse;
    }

    public TokenDataResponse refreshToken(String refreshToken) {
        Token currentToken = tokenStore.findByRefreshToken(refreshToken);

        tokenStore.checkTokenValidity(refreshToken);

        final TokenDataResponse tokenDataResponse = TokenDataResponse.builder()
                .jwtToken(jwtService.generateAccessToken(currentToken.getUserId(), currentToken.getTokenType()))
                .refreshToken(jwtService.generateRefreshToken(currentToken.getUserId()))
                .validityMs(jwtConfiguration.getAccessTokenValidity())
                .build();

        this.asyncTokenStore.saveUserToken(tokenDataResponse, currentToken.getUserId(),
                currentToken.getClientLocation().getUuid(), currentToken.getTokenType());
        return tokenDataResponse;
    }

    public TokenDataResponse validateTwoFactorCode(AccountCodeRequest request, CurrentUser currentUser) {
        accountCodesService.validateAccountCode(request, currentUser);
        return generateTokenDataResponse(userService.findByUserId(currentUser.getId()), request.getClientLocation(), false);
    }

    public List<LoggedClientModel> getLoggedClients(CurrentUser user) {
        return tokenRepository.findByUserId(user.getId()).stream()
                .map(LoggedClientModel::new)
                .toList();
    }

    public BooleanResponse isTwoFARequired(CurrentUser user) {
        return new BooleanResponse(user.getTokenType() == TokenType.TWO_FA_TOKEN);
    }

    public void logout(CurrentUser user) {
        String jwtToken = user.getPassword().substring(AuthorizationInterceptor.BEARER_ATTRIBUTE.length());

        Optional<Token> tokenOptional = tokenRepository.findByJwtToken(jwtToken);

        if (tokenOptional.isEmpty())
            throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN);

        Token token = tokenOptional.get();

        tokenStore.blacklistToken(jwtToken);
        tokenStore.blacklistToken(token.getRefreshToken());
    }
}
