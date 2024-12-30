package is.rares.kumo.service.explore;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.explore.PathPointErrorCodes;
import is.rares.kumo.mapping.explore.PathPointMapping;
import is.rares.kumo.model.explore.PathPointModel;
import is.rares.kumo.repository.explore.PathPointRepository;
import is.rares.kumo.utils.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PathPointService {
    private final PathPointRepository pathPointRepository;

    private final PathPointMapping pathPointMapping;

    public Page<PathPointModel> get(String name, Pageable pageable) {
        var page = pathPointRepository.findByPathContainsIgnoreCase(name, pageable);

        return new PageImpl<>(page.stream().map(pathPointMapping::mapEntityToModel).toList(), pageable,
                page.getTotalElements());
    }

    public PathPointModel create(PathPointModel pathPointModel) {
        if (!FileUtils.directoryExists(pathPointModel.getPath()))
            throw new KumoException(PathPointErrorCodes.PATH_DOES_NOT_EXIST);

        var optional = pathPointRepository.findByPathAndRoot(pathPointModel.getPath(), pathPointModel.isRoot());
        if (optional.isPresent())
            throw new KumoException(PathPointErrorCodes.DUPLICATE_PATH_POINT);

        var pathPoint = pathPointMapping.mapModelToEntity(pathPointModel);

        pathPoint = pathPointRepository.save(pathPoint);

        return pathPointMapping.mapEntityToModel(pathPoint);
    }

    public PathPointModel update(PathPointModel pathPointModel) {
        if (!FileUtils.directoryExists(pathPointModel.getPath()))
            throw new KumoException(PathPointErrorCodes.PATH_DOES_NOT_EXIST);

        if (pathPointModel.getUuid() == null)
            throw new KumoException(PathPointErrorCodes.ID_MISSING);

        var pathPointOptional = pathPointRepository.findByUuid(pathPointModel.getUuid());

        if (pathPointOptional.isEmpty())
            throw new KumoException(PathPointErrorCodes.NOT_FOUND);

        var pathPoint = pathPointOptional.get();

        if ((!pathPoint.getPath().equals(pathPointModel.getPath()) || pathPoint.isRoot() != pathPointModel.isRoot()) &&
                (pathPointRepository.findByPathAndRoot(pathPointModel.getPath(), pathPointModel.isRoot()).isPresent()))
            throw new KumoException(PathPointErrorCodes.DUPLICATE_PATH_POINT);

        pathPoint.setPath(pathPointModel.getPath());
        pathPoint.setRoot(pathPointModel.isRoot());

        pathPoint = pathPointRepository.save(pathPoint);

        return pathPointMapping.mapEntityToModel(pathPoint);
    }

    public void delete(UUID id) {
        if (!pathPointRepository.existsById(id))
            throw new KumoException(PathPointErrorCodes.NOT_FOUND);

        pathPointRepository.deleteById(id);
    }
}

