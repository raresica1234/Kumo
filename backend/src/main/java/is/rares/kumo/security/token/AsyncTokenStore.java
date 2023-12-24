package is.rares.kumo.security.token;

import is.rares.kumo.controller.responses.TokenDataResponse;
import is.rares.kumo.security.domain.ClientLocation;
import is.rares.kumo.security.enums.TokenType;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

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
    public void saveUserToken(TokenDataResponse tokenDataResponse, UUID userId, ClientLocation clientLocation, TokenType tokenType) {
        this.tokenRepository.save(Token.builder()
                .jwtToken(tokenDataResponse.getJwtToken())
                .refreshToken(tokenDataResponse.getRefreshToken())
                .validityMs(tokenDataResponse.getValidityMs())
                .userId(userId)
                .clientLocation(clientLocation)
                .tokenType(tokenType)
                .build());
    }

    @Async
    public void saveUserToken(TokenDataResponse tokenDataResponse, UUID userId, UUID clientLocationId, TokenType tokenType) {
        this.tokenRepository.save(Token.builder()
                .jwtToken(tokenDataResponse.getJwtToken())
                .refreshToken(tokenDataResponse.getRefreshToken())
                .validityMs(tokenDataResponse.getValidityMs())
                .userId(userId)
                .clientLocationId(clientLocationId)
                .tokenType(tokenType)
                .build());
    }

    @Async
    @Transactional
    public void updateTokenUsage(String jwt) {
        this.tokenRepository.updateTokenUsage(new Date(), jwt);
    }

}
