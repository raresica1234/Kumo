package is.rares.kumo.service;

import io.jsonwebtoken.Claims;
import is.rares.kumo.controller.requests.user.CreateRegisterInviteRequest;
import is.rares.kumo.controller.responses.user.RegisterInviteResponse;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AuthorizationErrorCodes;
import is.rares.kumo.domain.user.RegisterInvite;
import is.rares.kumo.domain.user.RegisterInviteStatus;
import is.rares.kumo.repository.user.RegisterInviteRepository;
import is.rares.kumo.security.domain.CurrentUser;
import is.rares.kumo.security.enums.TokenClaims;
import is.rares.kumo.security.services.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static is.rares.kumo.security.AuthorizationInterceptor.BEARER_ATTRIBUTE;

@Service

public class RegisterInviteService {
    private final RegisterInviteRepository registerInviteRepository;
    private final JwtService jwtService;

    public RegisterInviteService(RegisterInviteRepository registerInviteRepository,
                                 JwtService jwtService) {
        this.registerInviteRepository = registerInviteRepository;
        this.jwtService = jwtService;
    }

    public RegisterInviteResponse createRegisterInvite(CreateRegisterInviteRequest createRegisterInviteRequest) {
        long validity = createRegisterInviteRequest.getValiditySeconds();
        if (validity == 0)
            validity = 100 * 365 * 24 * 60 * 60L; // 100 years

        String jwt = jwtService.createRegisterInviteToken(validity, createRegisterInviteRequest.getUses());

        RegisterInvite registerInvite = new RegisterInvite();
        registerInvite.setJwtToken(jwt);
        registerInvite.setUsageCount(createRegisterInviteRequest.getUses());
        registerInvite.setStatus(RegisterInviteStatus.VALID);
        registerInvite.setExpireDate(LocalDateTime.now().plusSeconds(validity));

        registerInviteRepository.save(registerInvite);

        return new RegisterInviteResponse(jwt);
    }

    public boolean validateInvite(String registerInviteString) {
        Optional<RegisterInvite> registerInviteOptional = registerInviteRepository.findByJwtToken(registerInviteString);
        if (registerInviteOptional.isEmpty())
            return false;

        RegisterInvite registerInvite = registerInviteOptional.get();

        return registerInvite.getStatus().equals(RegisterInviteStatus.VALID);
    }

    @Transactional
    public void incrementInviteUsage(String registerInviteString) {
        Optional<RegisterInvite> registerInviteOptional = registerInviteRepository.findByJwtToken(registerInviteString);
        if (registerInviteOptional.isEmpty())
            throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN, "Invalid register invite.");

        try {
            Claims claims = jwtService.extractAllClaims(registerInviteString);

            Integer maxUsage = (Integer) claims.get(TokenClaims.MAX_USAGE.getClaim());

            RegisterInvite registerInvite = registerInviteOptional.get();

            registerInvite.setUsageCount(registerInvite.getUsageCount() + 1);
            if (registerInvite.getUsageCount() >= maxUsage && maxUsage != 0)
                registerInvite.setStatus(RegisterInviteStatus.INVALID);

            registerInviteRepository.updateUsageCountAndStatusByUuid(registerInvite.getUuid(),
                    registerInvite.getUsageCount(), registerInvite.getStatus());

        } catch (Exception e) {
            throw new KumoException(AuthorizationErrorCodes.INVALID_TOKEN, "Invalid register invite.");
        }
    }

    public boolean validateInviteForUser(CurrentUser user) {
        return validateInvite(user.getPassword().substring(BEARER_ATTRIBUTE.length()));
    }
}
