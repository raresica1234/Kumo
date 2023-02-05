package is.rares.kumo.security.token;

import is.rares.kumo.controller.responses.TokenDataResponse;
import is.rares.kumo.security.entity.ClientLocation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@EnableAsync
@Service
public class AsyncTokenStore {
    private final TokenRepository tokenRepository;

    public AsyncTokenStore(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Async
    public void saveUserToken(TokenDataResponse tokenDataResponse, UUID userId, ClientLocation clientLocation) {
        this.tokenRepository.save(Token.builder()
                .jwtToken(tokenDataResponse.getJwtToken())
                .refreshToken(tokenDataResponse.getRefreshToken())
                .validityMs(tokenDataResponse.getValidityMs())
                .userId(userId)
                .clientLocation(clientLocation)
                .build());
    }

    @Async
    @Transactional
    public void updateTokenUsage(String jwt) {
        this.tokenRepository.updateTokenUsage(new Date(), jwt);
    }

}
