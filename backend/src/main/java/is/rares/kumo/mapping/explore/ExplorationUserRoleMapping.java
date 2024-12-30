package is.rares.kumo.mapping.explore;

import is.rares.kumo.core.config.GlobalMapperConfig;
import is.rares.kumo.domain.user.User;
import is.rares.kumo.model.explore.ExplorationUserRoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface ExplorationUserRoleMapping {
    @Mapping(target = "roles", source = "explorationRoles")
    ExplorationUserRoleModel mapEntityToModel(User user);
}
