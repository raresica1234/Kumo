package is.rares.kumo.service;

import is.rares.kumo.security.enums.Feature;
import is.rares.kumo.domain.user.Role;
import is.rares.kumo.repository.user.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getOwnerRoleOrCreateIfNotExists() {
        var ownerRoleOptional = roleRepository.findByName("Owner");
        if (ownerRoleOptional.isPresent()) {
            Role ownerRole = ownerRoleOptional.get();
            if (ownerRole.getFeatures().stream().noneMatch(feature -> feature.equals(Feature.OWNER))) {
                log.error("Already existing owner role does not have owner feature");
                System.exit(0);
            }

            return ownerRole;
        }

        Role ownerRole = new Role();
        ownerRole.setName("Owner");
        ownerRole.getFeatures().add(Feature.OWNER);

        return roleRepository.save(ownerRole);
    }
}
