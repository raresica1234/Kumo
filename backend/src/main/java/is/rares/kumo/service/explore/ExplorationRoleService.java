package is.rares.kumo.service.explore;

import is.rares.kumo.mapping.explore.ExplorationRoleMapping;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.explore.ExplorationRoleErrorCodes;
import is.rares.kumo.model.explore.ExplorationRoleModel;
import is.rares.kumo.repository.explore.ExplorationRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExplorationRoleService {
    private final ExplorationRoleRepository explorationRoleRepository;

    private final ExplorationRoleMapping explorationRoleMapping;

    public ExplorationRoleService(ExplorationRoleRepository explorationRoleRepository,
                                  ExplorationRoleMapping explorationRoleMapping) {
        this.explorationRoleRepository = explorationRoleRepository;
        this.explorationRoleMapping = explorationRoleMapping;
    }

    public Page<ExplorationRoleModel> get(String name, Pageable pageable) {
        var page = explorationRoleRepository.findByNameContainsIgnoreCase(name, pageable);

        return new PageImpl<>(page.stream().map(explorationRoleMapping::mapEntityToModel).toList(), pageable,
                page.getTotalElements());
    }

    public ExplorationRoleModel create(ExplorationRoleModel model) {
        var optional = explorationRoleRepository.findByName(model.getName());
        if (optional.isPresent())
            throw new KumoException(ExplorationRoleErrorCodes.DUPLICATE_ROLE);

        var exploreRole = explorationRoleMapping.mapModelToEntity(model);

        exploreRole = explorationRoleRepository.save(exploreRole);

        return explorationRoleMapping.mapEntityToModel(exploreRole);
    }

    public ExplorationRoleModel update(ExplorationRoleModel model) {
        if (model.getUuid() == null)
            throw new KumoException(ExplorationRoleErrorCodes.ID_MISSING);

        var optional = explorationRoleRepository.findByUuid(model.getUuid());

        if (optional.isEmpty())
            throw new KumoException(ExplorationRoleErrorCodes.NOT_FOUND);

        var explorationRole = optional.get();

        if ((!explorationRole.getName().equals(model.getName()) &&
                (explorationRoleRepository.findByName(model.getName()).isPresent())))
            throw new KumoException(ExplorationRoleErrorCodes.DUPLICATE_ROLE);

        explorationRole.setName(model.getName());

        explorationRole = explorationRoleRepository.save(explorationRole);

        return explorationRoleMapping.mapEntityToModel(explorationRole);
    }
    
    public void delete(UUID id) {
        if (!explorationRoleRepository.existsById(id))
            throw new KumoException(ExplorationRoleErrorCodes.NOT_FOUND);

        explorationRoleRepository.deleteById(id);
    }
}
