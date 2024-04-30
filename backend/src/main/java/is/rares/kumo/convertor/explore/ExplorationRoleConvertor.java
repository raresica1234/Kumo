package is.rares.kumo.convertor.explore;

import is.rares.kumo.domain.explore.ExplorationRole;
import is.rares.kumo.model.explore.ExplorationRoleModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ExplorationRoleConvertor {
    private final ModelMapper modelMapper;

    public ExplorationRoleConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ExplorationRoleModel mapEntityToModel(ExplorationRole explorationRole) {
        return modelMapper.map(explorationRole, ExplorationRoleModel.class);
    }

    public ExplorationRole mapModelToEntity(ExplorationRoleModel explorationRoleModel) {
        return modelMapper.map(explorationRoleModel, ExplorationRole.class);
    }
}

