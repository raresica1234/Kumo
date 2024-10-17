package is.rares.kumo.service.explore;

import is.rares.kumo.convertor.explore.PermissionConvertor;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.explore.ExplorationRoleErrorCodes;
import is.rares.kumo.core.exceptions.codes.explore.PathPointErrorCodes;
import is.rares.kumo.core.exceptions.codes.explore.PermissionErrorCodes;
import is.rares.kumo.model.explore.PermissionModel;
import is.rares.kumo.model.explore.PermissionShortModel;
import is.rares.kumo.repository.explore.ExplorationRoleRepository;
import is.rares.kumo.repository.explore.PathPointRepository;
import is.rares.kumo.repository.explore.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionConvertor permissionConvertor;
    private final PathPointRepository pathPointRepository;
    private final ExplorationRoleRepository explorationRoleRepository;

    public PermissionService(PermissionRepository permissionRepository,
                             PermissionConvertor permissionConvertor,
                             PathPointRepository pathPointRepository,
                             ExplorationRoleRepository explorationRoleRepository) {
        this.permissionRepository = permissionRepository;
        this.permissionConvertor = permissionConvertor;
        this.pathPointRepository = pathPointRepository;
        this.explorationRoleRepository = explorationRoleRepository;
    }

    public Page<PermissionModel> get(String path, String role, Pageable pageable) {
        var result =
                permissionRepository.findByPathAndRole(path, role, pageable);
        return new PageImpl<>(result.stream().map(permissionConvertor::mapEntityToModel).toList(), pageable, result.getTotalElements());
    }

    public PermissionShortModel create(PermissionShortModel model) {
        var pathPointOptional = pathPointRepository.findByUuid(model.getPathPointId());
        if (pathPointOptional.isEmpty())
            throw new KumoException(PathPointErrorCodes.NOT_FOUND);

        var explorationRoleOptional = explorationRoleRepository.findByUuid(model.getExplorationRoleId());
        if (explorationRoleOptional.isEmpty())
            throw new KumoException(ExplorationRoleErrorCodes.NOT_FOUND);

        if (permissionRepository.existsByPathPoint_UuidAndExplorationRole_Uuid(model.getPathPointId(), model.getExplorationRoleId()))
            throw new KumoException(PermissionErrorCodes.DUPLICATE_PERMISSION);

        var domainEntity = permissionConvertor.mapShortModelToEntity(model);

        domainEntity.setPathPoint(pathPointOptional.get());
        domainEntity.setExplorationRole(explorationRoleOptional.get());

        permissionRepository.save(domainEntity);

        return permissionConvertor.mapEntityToShortModel(domainEntity);
    }

    public PermissionShortModel update(PermissionShortModel model) {
        if (model.getUuid() == null)
            throw new KumoException(PermissionErrorCodes.ID_MISSING);

        var permissionOptional = permissionRepository.findByUuid(model.getUuid());

        if (permissionOptional.isEmpty())
            throw new KumoException(PermissionErrorCodes.NOT_FOUND);

        var pathPointOptional = pathPointRepository.findByUuid(model.getPathPointId());
        if (pathPointOptional.isEmpty())
            throw new KumoException(PathPointErrorCodes.NOT_FOUND);

        var explorationRoleOptional = explorationRoleRepository.findByUuid(model.getExplorationRoleId());
        if (explorationRoleOptional.isEmpty())
            throw new KumoException(ExplorationRoleErrorCodes.NOT_FOUND);

        var domainEntity = permissionOptional.get();

        if ((!model.getPathPointId().equals(domainEntity.getPathPointId()) || !model.getExplorationRoleId().equals(domainEntity.getExplorationRoleId())) &&
                (permissionRepository.existsByPathPoint_UuidAndExplorationRole_Uuid(model.getPathPointId(), model.getExplorationRoleId()))) {
            throw new KumoException(PermissionErrorCodes.DUPLICATE_PERMISSION);
        }

        domainEntity.setPathPoint(pathPointOptional.get());
        domainEntity.setExplorationRole(explorationRoleOptional.get());
        domainEntity.setRead(model.isRead());
        domainEntity.setWrite(model.isWrite());
        domainEntity.setDelete(model.isDelete());
        domainEntity.setModifyRoot(model.isModifyRoot());

        permissionRepository.save(domainEntity);

        return permissionConvertor.mapEntityToShortModel(domainEntity);
    }
    
    public void delete(UUID id) {
        if (!permissionRepository.existsById(id))
            throw new KumoException(PermissionErrorCodes.NOT_FOUND);

        permissionRepository.deleteById(id);
    }
}
