package is.rares.kumo.mapping.explore;

import is.rares.kumo.core.config.GlobalMapperConfig;
import is.rares.kumo.domain.user.User;
import is.rares.kumo.model.explore.ExplorationUserRoleModel;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface ExplorationUserRoleMapping {
    ExplorationUserRoleModel mapEntityToModel(User user);
}
