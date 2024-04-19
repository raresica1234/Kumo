package is.rares.kumo.convertor.explore;

import is.rares.kumo.domain.explore.PathPoint;
import is.rares.kumo.model.explore.PathPointModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PathPointConvertor {
    private final ModelMapper modelMapper;

    public PathPointConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PathPointModel mapEntityToModel(PathPoint pathPoint) {
        return modelMapper.map(pathPoint, PathPointModel.class);
    }

    public PathPoint mapModelToEntity(PathPointModel pathPointModel) {
        return modelMapper.map(pathPointModel, PathPoint.class);
    }
}
