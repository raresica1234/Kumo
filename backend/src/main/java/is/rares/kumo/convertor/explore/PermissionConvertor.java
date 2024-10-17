package is.rares.kumo.convertor.explore;

import is.rares.kumo.domain.explore.Permission;
import is.rares.kumo.model.explore.PermissionModel;
import is.rares.kumo.model.explore.PermissionShortModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PermissionConvertor {
    private final ModelMapper modelMapper;

    private final PathPointConvertor pathPointConvertor;
    private final ExplorationRoleConvertor explorationRoleConvertor;

    public PermissionConvertor(ModelMapper modelMapper,
                               PathPointConvertor pathPointConvertor,
                               ExplorationRoleConvertor explorationRoleConvertor) {
        this.modelMapper = modelMapper;
        this.pathPointConvertor = pathPointConvertor;
        this.explorationRoleConvertor = explorationRoleConvertor;
    }

    public PermissionModel mapEntityToModel(Permission permission) {
        var model = modelMapper.map(permission, PermissionModel.class);

        model.setExplorationRole(explorationRoleConvertor.mapEntityToModel(permission.getExplorationRole()));
        model.setPathPoint(pathPointConvertor.mapEntityToModel(permission.getPathPoint()));

        return model;
    }

    public PermissionShortModel mapEntityToShortModel(Permission permission) {
        return modelMapper.map(permission, PermissionShortModel.class);
    }

    public Permission mapShortModelToEntity(PermissionShortModel permissionShortModel) {
        return modelMapper.map(permissionShortModel, Permission.class);
    }
}
