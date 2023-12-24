package is.rares.kumo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import is.rares.kumo.core.exceptions.codes.AuthorizationErrorCodes;
import is.rares.kumo.security.domain.CurrentUser;
import is.rares.kumo.security.enums.TokenType;
import is.rares.kumo.security.enums.UserClaims;
import is.rares.kumo.security.services.KeyLoaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class JwtUserService extends KeyLoaderService implements UserDetailsService {
    @Override
    public CurrentUser loadUserByUsername(String jwt) throws UsernameNotFoundException {
        try {
            final Claims userClaims = extractAllClaims(jwt);

            final String username = userClaims.getSubject();
            final UUID userId = UUID.fromString(username);
            final Date expirationDate = userClaims.getExpiration();

            if (!userClaims.containsKey(UserClaims.TOKEN_TYPE.getClaim()))
                throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN, "Invalid token");

            TokenType tokenType = TokenType.valueOf((String) userClaims.get(UserClaims.TOKEN_TYPE.getClaim()));

            return new CurrentUser(userId, username, "Bearer " + jwt, new ArrayList<>(), tokenType, expirationDate);

        } catch (Exception e) {
            throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN, "Invalid token");
        }
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
}
