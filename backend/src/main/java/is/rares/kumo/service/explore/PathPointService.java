package is.rares.kumo.service.explore;

import is.rares.kumo.convertor.explore.PathPointConvertor;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.PathPointErrorCodes;
import is.rares.kumo.model.explore.PathPointModel;
import is.rares.kumo.repository.explore.PathPointRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PathPointService {
    private final PathPointRepository pathPointRepository;

    private final PathPointConvertor pathPointConvertor;

    public PathPointService(PathPointRepository pathPointRepository, PathPointConvertor pathPointConvertor) {
        this.pathPointRepository = pathPointRepository;
        this.pathPointConvertor = pathPointConvertor;
    }


    public PathPointModel createPathPoint(PathPointModel pathPointModel) {
        var optional = pathPointRepository.findByPathAndRoot(pathPointModel.getPath(), pathPointModel.isRoot());
        if (optional.isPresent())
            throw new KumoException(PathPointErrorCodes.DUPLICATE_PATH_POINT);

        var pathPoint = pathPointConvertor.mapModelToEntity(pathPointModel);

        pathPoint = pathPointRepository.save(pathPoint);

        return pathPointConvertor.mapEntityToModel(pathPoint);
    }

    public PathPointModel updatePathPoint(PathPointModel pathPointModel) {
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

        return pathPointConvertor.mapEntityToModel(pathPoint);
    }

    public void deletePathPoint(UUID id) {
        if (!pathPointRepository.existsById(id))
            throw new KumoException(PathPointErrorCodes.NOT_FOUND);

        pathPointRepository.deleteById(id);
    }

    public Page<PathPointModel> getPathPoints(String name, Pageable pageable) {
        var page = pathPointRepository.findByPathContainsIgnoreCase(name, pageable);

        return new PageImpl<>(page.stream().map(pathPointConvertor::mapEntityToModel).toList(), pageable,
                page.getTotalElements());
    }
}

