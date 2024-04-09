package is.rares.kumo.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import is.rares.kumo.core.config.JWTConfiguration;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import is.rares.kumo.security.enums.TokenClaims;
import is.rares.kumo.security.enums.TokenType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService extends KeyLoaderService{
    private final JWTConfiguration jwtConfiguration;

    public JwtService(JWTConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    public String createToken(Map<String, Object> claims, String subject, long validity) {
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


    public Claims extractAllClaims(String token) {
        if (publicKey == null)
            throw new KumoException(AccountCodeErrorCodes.UNEXPECTED_ERROR, "Key missing");

        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(UUID userId, TokenType tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TokenClaims.TOKEN_TYPE.getClaim(), tokenType);
        return createToken(claims, userId.toString(), jwtConfiguration.getAccessTokenValidity());
    }

    public String generateRefreshToken(UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TokenClaims.RANDOM_ID.getClaim(), UUID.randomUUID().toString());
        claims.put(TokenClaims.REFRESH_TOKEN.getClaim(), true);
        return createToken(claims, userId.toString(), jwtConfiguration.getRefreshTokenValidity());
    }

    public String createRegisterInviteToken(long validity, int uses) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TokenClaims.RANDOM_ID.getClaim(), UUID.randomUUID().toString());
        claims.put(TokenClaims.TOKEN_TYPE.getClaim(), TokenType.REGISTER_INVITE_TOKEN);
        claims.put(TokenClaims.MAX_USAGE.getClaim(), uses);

        return createToken(claims, "Register Invite", validity * 1000L);
    }
}
