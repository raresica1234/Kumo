package is.rares.kumo.security.token;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenStore {
    private final TokenRepository tokenRepository;

    private final RedisTemplate<Object, Object> redisTemplate;

    public TokenStore(TokenRepository tokenRepository,
                      RedisTemplate<Object, Object> redisTemplate) {
        this.tokenRepository = tokenRepository;
        this.redisTemplate = redisTemplate;
    }

    public Token fetchByRefreshToken(String refreshToken) {
        return this.tokenRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new KumoException(AccountCodeErrorCodes.UNEXPECTED_ERROR, "Invalid sign-in"));
    }

    public void checkTokenValidity(String jwt) {
//        redisTemplate.opsForValue().get();
    }

}


