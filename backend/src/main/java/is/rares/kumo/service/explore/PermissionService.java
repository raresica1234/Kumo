package is.rares.kumo.service.explore;

import is.rares.kumo.domain.explore.Permission;
import is.rares.kumo.mapping.explore.PermissionMapping;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.explore.PermissionErrorCodes;
import is.rares.kumo.model.explore.PermissionModel;
import is.rares.kumo.model.explore.PermissionShortModel;
import is.rares.kumo.repository.explore.PermissionRepository;
import is.rares.kumo.security.domain.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapping permissionMapping;
    private final PathPointService pathPointService;
    private final ExplorationRoleService explorationRoleService;

    public Page<PermissionModel> get(String path, String role, Pageable pageable) {
        var result =
                permissionRepository.findByPathAndRole(path, role, pageable);
        return new PageImpl<>(result.stream().map(permissionMapping::mapEntityToModel).toList(), pageable, result.getTotalElements());
    }

    public PermissionShortModel create(PermissionShortModel model) {
        var pathPoint = pathPointService.getByUuid(model.getPathPointId());
        var explorationRole = explorationRoleService.getByUuid(model.getExplorationRoleId());

        if (permissionRepository.existsByPathPoint_UuidAndExplorationRole_Uuid(model.getPathPointId(), model.getExplorationRoleId()))
            throw new KumoException(PermissionErrorCodes.DUPLICATE_PERMISSION);

        if (pathPoint.isRoot() && !model.isRead())
            throw new KumoException(PermissionErrorCodes.ROOT_PATHS_MUST_BE_READABLE);

        var domainEntity = permissionMapping.mapShortModelToEntity(model);

        domainEntity.setPathPoint(pathPoint);
        domainEntity.setExplorationRole(explorationRole);

        permissionRepository.save(domainEntity);

        return permissionMapping.mapEntityToShortModel(domainEntity);
    }

    public PermissionShortModel update(PermissionShortModel model) {
        if (model.getUuid() == null)
            throw new KumoException(PermissionErrorCodes.ID_MISSING);

        var permissionOptional = permissionRepository.findByUuid(model.getUuid());

        if (permissionOptional.isEmpty())
            throw new KumoException(PermissionErrorCodes.NOT_FOUND);

        var pathPoint = pathPointService.getByUuid(model.getPathPointId());
        var explorationRole = explorationRoleService.getByUuid(model.getExplorationRoleId());

        var domainEntity = permissionOptional.get();

        if ((!model.getPathPointId().equals(domainEntity.getPathPointId()) || !model.getExplorationRoleId().equals(domainEntity.getExplorationRoleId())) &&
                (permissionRepository.existsByPathPoint_UuidAndExplorationRole_Uuid(model.getPathPointId(), model.getExplorationRoleId()))) {
            throw new KumoException(PermissionErrorCodes.DUPLICATE_PERMISSION);
        }

        domainEntity.setPathPoint(pathPoint);
        domainEntity.setExplorationRole(explorationRole);
        domainEntity.setRead(model.isRead());
        domainEntity.setWrite(model.isWrite());
        domainEntity.setDelete(model.isDelete());
        domainEntity.setModifyRoot(model.isModifyRoot());

        permissionRepository.save(domainEntity);

        return permissionMapping.mapEntityToShortModel(domainEntity);
    }
    
    public void delete(UUID id) {
        if (!permissionRepository.existsById(id))
            throw new KumoException(PermissionErrorCodes.NOT_FOUND);

        permissionRepository.deleteById(id);
    }

}
