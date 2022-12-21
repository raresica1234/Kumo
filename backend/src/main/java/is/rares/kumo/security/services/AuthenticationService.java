package is.rares.kumo.security.services;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import is.rares.kumo.core.config.JWTConfiguration;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import is.rares.kumo.domain.User;
import is.rares.kumo.model.requests.LoginRequest;
import is.rares.kumo.model.responses.TokenDataResponse;
import is.rares.kumo.repository.UserRepository;
import is.rares.kumo.security.entity.ClientLocation;
import is.rares.kumo.security.entity.CurrentUser;
import is.rares.kumo.security.enums.UserClaims;
import is.rares.kumo.security.token.AsyncTokenStore;
import is.rares.kumo.security.token.Token;
import is.rares.kumo.security.token.TokenStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class AuthenticationService extends KeyLoaderService {
    private final JWTConfiguration jwtConfiguration;
    private final UserRepository userRepository;
    private final TokenStore tokenStore;
    private final AsyncTokenStore asyncTokenStore;

    @Autowired
    public AuthenticationService(JWTConfiguration jwtConfiguration, UserRepository userRepository, TokenStore tokenStore, AsyncTokenStore asyncTokenStore) {
        this.jwtConfiguration = jwtConfiguration;
        this.userRepository = userRepository;
        this.tokenStore = tokenStore;
        this.asyncTokenStore = asyncTokenStore;
    }

    private String generateToken(UUID userId, boolean isUsing2FA, boolean isFirstLogin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(UserClaims.IS_USING_TWO_FA.getClaim(), isUsing2FA);
        if (isFirstLogin)
            claims.put(UserClaims.TWO_FA_NEEDED.getClaim(), true);
        return createToken(claims, userId.toString(), jwtConfiguration.getAccessTokenValidity());
    }

    private String createToken(Map<String, Object> claims, String subject, int validity) {
        final JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(jwtConfiguration.getIssuer())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .setId(UUID.randomUUID().toString());
        try {
            jwtBuilder.signWith(this.privateKey, SignatureAlgorithm.ES512);
        } catch (Exception e) {
            throw new KumoException(AccountCodeErrorCodes.UNEXPECTED_ERROR, "Signing error");
        }
        return jwtBuilder.compact();
    }

    private String generateRefreshToken(UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("randomId", UUID.randomUUID().toString());
        return createToken(claims, userId.toString(), jwtConfiguration.getRefreshTokenValidity());
    }

    public TokenDataResponse login(LoginRequest request) {
        final User user = this.userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new KumoException(AccountCodeErrorCodes.USERNAME_NOT_FOUND, "Username not found"));

        if (!user.getPassword().equals(request.getPassword()))
            throw new KumoException(AccountCodeErrorCodes.PASSWORD_INCORRECT, "Password incorrect");

        return this.generateTokenDataResponse(user, request.getClientLocation(), true);
    }

    public TokenDataResponse generateTokenDataResponse(User user, ClientLocation clientLocation, boolean isFirstLogin) {
        final TokenDataResponse tokenDataResponse = TokenDataResponse.builder()
                .jwtToken(generateToken(user.getUuid(), user.isUsing2FA(), isFirstLogin))
                .refreshToken(generateRefreshToken(user.getUuid()))
                .validityMs(jwtConfiguration.getAccessTokenValidity())
                .build();

        if (user.isUsing2FA()) {
            // TODO: generate 2fa
        }

        this.asyncTokenStore.saveUserToken(tokenDataResponse, user.getUuid(), clientLocation);
        return tokenDataResponse;
    }

    public TokenDataResponse refreshToken(CurrentUser currentUser, String refreshToken) {
        Token currentToken = this.tokenStore.fetchByRefreshToken(refreshToken);

        final TokenDataResponse tokenDataResponse = TokenDataResponse.builder()
                .jwtToken(generateToken(currentUser.getId(), currentUser.isUsing2FA(), false))
                .refreshToken(generateRefreshToken(currentUser.getId()))
                .validityMs(jwtConfiguration.getAccessTokenValidity())
                .build();

        this.asyncTokenStore.saveUserToken(tokenDataResponse, currentUser.getId(), currentToken.getClientLocation());
        return tokenDataResponse;
    }

}
