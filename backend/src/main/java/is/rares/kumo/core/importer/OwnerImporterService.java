package is.rares.kumo.core.importer;

import io.micrometer.common.util.StringUtils;
import is.rares.kumo.core.config.KumoConfig;
import is.rares.kumo.domain.user.Role;
import is.rares.kumo.domain.user.User;
import is.rares.kumo.repository.user.RoleRepository;
import is.rares.kumo.repository.user.UserRepository;
import is.rares.kumo.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OwnerImporterService {
    private final RoleRepository roleRepository;

    private final KumoConfig kumoConfig;
    private final RoleService roleService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OwnerImporterService(KumoConfig kumoConfig, RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder,
                                RoleRepository roleRepository) {
        this.kumoConfig = kumoConfig;
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void registerOwner() {
        if (StringUtils.isEmpty(kumoConfig.getOwnerUsername())) {
            log.warn("Owner username not set. No users will have privileged access unless manually created.");
            return;
        }

        Optional<User> ownerOptional = userRepository.findByUsername(kumoConfig.getOwnerUsername());
        if (ownerOptional.isPresent()) {
            User owner = ownerOptional.get();
            if (!StringUtils.isEmpty(kumoConfig.getOwnerEmail()) && !owner.getEmail().equals(kumoConfig.getOwnerEmail())) {
                log.error("Email and username don't match the already existing account");
                System.exit(0);
            }

            if (owner.getRoles().stream().noneMatch(role -> role.getName().equals("Owner"))) {
                Role ownerRole = roleService.getOwnerRoleOrCreateIfNotExists();
                ownerRole.getUsers().add(owner);
                roleRepository.save(ownerRole);
            }
        } else {
            if (StringUtils.isEmpty(kumoConfig.getOwnerEmail()) || StringUtils.isEmpty(kumoConfig.getOwnerPassword())) {
                log.error("Could not create owner account because email or password not set.");
                return;
            }
            User owner = new User();
            owner.setUsername(kumoConfig.getOwnerUsername());
            owner.setEmail(kumoConfig.getOwnerEmail());
            owner.setPassword(passwordEncoder.encode(kumoConfig.getOwnerPassword()));
            owner = userRepository.save(owner);


            Role ownerRole = roleService.getOwnerRoleOrCreateIfNotExists();
            ownerRole.getUsers().add(owner);
            roleRepository.save(ownerRole);
        }
    }
}
