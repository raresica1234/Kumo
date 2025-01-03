package is.rares.kumo.service.explore;

import is.rares.kumo.controller.requests.explore.SetUserExplorationRolesRequest;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.explore.ExplorationUserRoleErrorCodes;
import is.rares.kumo.domain.user.User;
import is.rares.kumo.mapping.explore.ExplorationUserRoleMapping;
import is.rares.kumo.model.explore.ExplorationUserRoleModel;
import is.rares.kumo.repository.explore.ExplorationRoleRepository;
import is.rares.kumo.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ExplorationUserRoleService {
    private final UserRepository userRepository;
    private final ExplorationRoleRepository explorationRoleRepository;
    private final ExplorationUserRoleMapping mapping;

    public Page<ExplorationUserRoleModel> get(String username, String roleName, Pageable pageable) {
        Page<User> page;
        if (roleName.isEmpty())
            page = userRepository.findByUsernameContainsIgnoreCase(username, pageable);
        else
            page = userRepository.findByUsernameContainsIgnoreCaseAndExplorationRoles_NameContainsIgnoreCase(username, roleName, pageable);

        return new PageImpl<>(page.stream().map(mapping::mapEntityToModel).toList(), pageable,
                page.getTotalElements());
    }

    @Transactional
    public void unassignUserExplorationRole(UUID user, UUID role) {
        var userOptional = userRepository.findByUuid(user);
        if (userOptional.isEmpty())
            throw new KumoException(ExplorationUserRoleErrorCodes.USER_NOT_FOUND);

        var explorationRoleOptional = explorationRoleRepository.findByUuid(role);
        if (explorationRoleOptional.isEmpty())
            throw new KumoException(ExplorationUserRoleErrorCodes.EXPLORATION_ROLE_NOT_FOUND);

        if (!userRepository.existsByUuidAndExplorationRoles_Uuid(user, role))
            throw new KumoException(ExplorationUserRoleErrorCodes.USER_EXPLORATION_ROLE_NOT_FOUND);

        userRepository.deleteUserExplorationRole(user, role);
    }

    @Transactional
    public ExplorationUserRoleModel setUserExplorationRoles(SetUserExplorationRolesRequest model) {
        if (!userRepository.existsByUuid(model.getUuid()))
            throw new KumoException(ExplorationUserRoleErrorCodes.USER_NOT_FOUND);

        userRepository.deleteAllExplorationRolesForUser(model.getUuid());

        for (var explorationRoleId : model.getExplorationRoles()) {
            if (!explorationRoleRepository.existsByUuid(explorationRoleId))
                throw new KumoException(ExplorationUserRoleErrorCodes.EXPLORATION_ROLE_NOT_FOUND);

            userRepository.addUserExplorationRole(model.getUuid(), explorationRoleId);
        }

        var user = userRepository.findByUuid(model.getUuid()).orElseThrow();
        return mapping.mapEntityToModel(user);
    }
}
