package is.rares.kumo.security.services;

import io.jsonwebtoken.Claims;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AuthorizationErrorCodes;
import is.rares.kumo.security.domain.CurrentUser;
import is.rares.kumo.security.enums.TokenClaims;
import is.rares.kumo.security.enums.TokenType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class JwtUserService implements UserDetailsService {
    private final JwtService jwtService;

    public JwtUserService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public CurrentUser loadUserByUsername(String jwt) throws UsernameNotFoundException {
        try {
            final Claims userClaims = jwtService.extractAllClaims(jwt);

            final String username = userClaims.getSubject();

            if (!userClaims.containsKey(TokenClaims.TOKEN_TYPE.getClaim()))
                throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN, "Invalid token");

            TokenType tokenType = TokenType.valueOf((String) userClaims.get(TokenClaims.TOKEN_TYPE.getClaim()));

            UUID userId;
            if (tokenType.equals(TokenType.REGISTER_INVITE_TOKEN))
                userId = UUID.randomUUID();
            else
                userId = UUID.fromString(username);

            final Date expirationDate = userClaims.getExpiration();


            return new CurrentUser(userId, username, "Bearer " + jwt, new ArrayList<>(), tokenType, expirationDate);

        } catch (Exception e) {
            throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN, "Invalid token");
        }
    }

}
