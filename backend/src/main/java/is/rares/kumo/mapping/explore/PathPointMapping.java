package is.rares.kumo.mapping.explore;

import is.rares.kumo.core.config.GlobalMapperConfig;
import is.rares.kumo.domain.explore.PathPoint;
import is.rares.kumo.model.explore.PathPointModel;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface PathPointMapping {
    PathPointModel mapEntityToModel(PathPoint pathPoint);
    PathPoint mapModelToEntity(PathPointModel pathPointModel);
}