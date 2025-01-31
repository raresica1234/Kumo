package is.rares.kumo.security.token;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AuthorizationErrorCodes;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenStore {
    private final TokenRepository tokenRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_DOCUMENT_NAME = "token::";

    public TokenStore(TokenRepository tokenRepository,
                      RedisTemplate<String, Object> redisTemplate) {
        this.tokenRepository = tokenRepository;
        this.redisTemplate = redisTemplate;
    }

    public Token findByRefreshToken(String refreshToken) {
        return this.tokenRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new KumoException(AuthorizationErrorCodes.INVALID_REFRESH_TOKEN, "Invalid refresh token"));
    }

    public void checkTokenValidity(String jwt) {
        if (redisTemplate.opsForValue().get(TOKEN_DOCUMENT_NAME + jwt) != null) // token was blacklisted
            throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN, "Invalid authentication");
    }

    public void blacklistToken(String jwt) {
        redisTemplate.opsForValue().set(TOKEN_DOCUMENT_NAME + jwt, jwt);
    }
}


