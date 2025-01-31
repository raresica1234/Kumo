package is.rares.kumo.security.token;

import java.util.Date;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import is.rares.kumo.controller.responses.user.TokenDataResponse;
import is.rares.kumo.security.domain.ClientLocation;
import is.rares.kumo.security.enums.TokenType;
import jakarta.transaction.Transactional;

@Service
public class AsyncTokenStore {
    private final TokenRepository tokenRepository;

    public AsyncTokenStore(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Async
    public void saveUserToken(TokenDataResponse tokenDataResponse, UUID userId, ClientLocation clientLocation, TokenType tokenType) {
        tokenRepository.save(Token.builder()
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
        Token entity = tokenRepository.save(Token.builder()
                .jwtToken(tokenDataResponse.getJwtToken())
                .refreshToken(tokenDataResponse.getRefreshToken())
                .validityMs(tokenDataResponse.getValidityMs())
                .userId(userId)
                .tokenType(tokenType)
                .build());

        tokenRepository.updateClientLocationIdByUuid(entity.getUuid(), clientLocationId);
    }

    @Async
    @Transactional
    public void updateTokenUsage(String jwt) {
        tokenRepository.updateTokenUsage(new Date(), jwt);
    }

}
