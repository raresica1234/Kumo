package is.rares.kumo.mapping.explore;

import is.rares.kumo.core.config.GlobalMapperConfig;
import is.rares.kumo.domain.explore.ExplorationRole;
import is.rares.kumo.model.explore.ExplorationRoleModel;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface ExplorationRoleMapping {
    ExplorationRoleModel mapEntityToModel(ExplorationRole explorationRole);

    ExplorationRole mapModelToEntity(ExplorationRoleModel explorationRoleModel);
}