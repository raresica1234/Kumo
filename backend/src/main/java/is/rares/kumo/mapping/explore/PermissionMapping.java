package is.rares.kumo.mapping.explore;

import is.rares.kumo.core.config.GlobalMapperConfig;
import is.rares.kumo.domain.explore.Permission;
import is.rares.kumo.model.explore.PermissionModel;
import is.rares.kumo.model.explore.PermissionShortModel;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface PermissionMapping {
    PermissionModel mapEntityToModel(Permission permission);

    PermissionShortModel mapEntityToShortModel(Permission permission);

    Permission mapShortModelToEntity(PermissionShortModel permissionShortModel);
}
