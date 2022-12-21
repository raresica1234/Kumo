package is.rares.kumo.security.token;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import org.springframework.stereotype.Service;

@Service
public class TokenStore {
    private final TokenRepository tokenRepository;

    public TokenStore(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token fetchByRefreshToken(String refreshToken) {
        return this.tokenRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new KumoException(AccountCodeErrorCodes.UNEXPECTED_ERROR, "Invalid sign-in"));
    }
}


