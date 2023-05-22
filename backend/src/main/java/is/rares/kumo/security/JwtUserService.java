package is.rares.kumo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import is.rares.kumo.security.domain.CurrentUser;
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

            boolean isRefreshToken = userClaims.get(UserClaims.REFRESH_TOKEN.getClaim()) != null;

            if (!isRefreshToken) {
                final boolean isUsing2FA = (boolean) userClaims.get(UserClaims.IS_USING_TWO_FA.getClaim());

                boolean twoFANeeded = false;
                if (userClaims.containsKey(UserClaims.TWO_FA_NEEDED.getClaim()))
                    twoFANeeded = (boolean) userClaims.get(UserClaims.TWO_FA_NEEDED.getClaim());
                return new CurrentUser(userId, username, "Bearer " + jwt, new ArrayList<>(), isUsing2FA, twoFANeeded, expirationDate);

            }
            else
                return new CurrentUser(userId, username, "Bearer " + jwt, new ArrayList<>(), false, false, expirationDate);

        } catch (Exception e) {
            throw new KumoException(AccountCodeErrorCodes.UNEXPECTED_ERROR, "Invalid token");
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
