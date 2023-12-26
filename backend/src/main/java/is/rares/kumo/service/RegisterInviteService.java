package is.rares.kumo.service;

import is.rares.kumo.controller.requests.user.CreateRegisterInviteRequest;
import is.rares.kumo.controller.responses.user.RegisterInviteResponse;
import is.rares.kumo.domain.user.RegisterInvite;
import is.rares.kumo.domain.user.RegisterInviteStatus;
import is.rares.kumo.repository.RegisterInviteRepository;
import is.rares.kumo.security.services.AuthenticationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class RegisterInviteService {
    private final AuthenticationService authenticationService;
    private final RegisterInviteRepository registerInviteRepository;

    public RegisterInviteService(AuthenticationService authenticationService, RegisterInviteRepository registerInviteRepository) {
        this.authenticationService = authenticationService;
        this.registerInviteRepository = registerInviteRepository;
    }

    public RegisterInviteResponse createRegisterInvite(CreateRegisterInviteRequest createRegisterInviteRequest) {
        String jwt = authenticationService.createRegisterInviteJwt(createRegisterInviteRequest.getValiditySeconds(), createRegisterInviteRequest.getUses());

        RegisterInvite registerInvite = new RegisterInvite();
        registerInvite.setJwtToken(jwt);
        registerInvite.setUsageCount(createRegisterInviteRequest.getUses());
        registerInvite.setStatus(RegisterInviteStatus.VALID);
        if (createRegisterInviteRequest.getValiditySeconds() != 0)
            registerInvite.setExpireDate(LocalDateTime.now().plusSeconds(createRegisterInviteRequest.getValiditySeconds()));
        registerInviteRepository.save(registerInvite);

        return new RegisterInviteResponse(jwt);
    }
}
