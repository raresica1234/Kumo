package is.rares.kumo.service;

import io.jsonwebtoken.Claims;
import is.rares.kumo.controller.requests.user.CreateRegisterInviteRequest;
import is.rares.kumo.controller.responses.user.RegisterInviteResponse;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AuthorizationErrorCodes;
import is.rares.kumo.domain.user.RegisterInvite;
import is.rares.kumo.domain.user.RegisterInviteStatus;
import is.rares.kumo.repository.RegisterInviteRepository;
import is.rares.kumo.security.services.JwtService;
import is.rares.kumo.security.services.JwtUserService;
import is.rares.kumo.security.services.AuthenticationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public void validateInvite(String registerInvite) {
        Optional<RegisterInvite> registerInviteOptional = registerInviteRepository.findByJwtToken(registerInvite);
        if (registerInviteOptional.isEmpty())
            throw new KumoException(AuthorizationErrorCodes.INVALID_REGISTER_INVITE);

        try {
            Claims claims = jwtService.extractAllClaims(registerInvite);

            System.out.println(claims);
        } catch(Exception e) {
            throw new KumoException(AuthorizationErrorCodes.INVALID_REGISTER_INVITE);
        }
    }
}
