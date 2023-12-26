package is.rares.kumo.service;

import is.rares.kumo.domain.user.Feature;
import is.rares.kumo.domain.user.Authority;
import is.rares.kumo.domain.user.Role;
import is.rares.kumo.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    private final AuthorityService authorityService;

    public RoleService(RoleRepository roleRepository, AuthorityService authorityService) {
        this.roleRepository = roleRepository;
        this.authorityService = authorityService;
    }

    public Role getOwnerRoleOrCreateIfNotExists() {
        var ownerRoleOptional = roleRepository.findByName("Owner");
        if (ownerRoleOptional.isPresent()) {
            Role ownerRole = ownerRoleOptional.get();
            if (ownerRole.getAuthorities().stream().noneMatch(authority -> authority.getFeature().equals(Feature.OWNER))) {
                log.error("Already existing owner role does not have owner feature");
                System.exit(0);
            }

            return ownerRole;
        }

        Authority authority = authorityService.getOwnerAuthorityOrCreateIfNotExists();

        Role ownerRole = new Role();
        ownerRole.setName("Owner");
        ownerRole.getAuthorities().add(authority);

        return roleRepository.save(ownerRole);
    }
}
