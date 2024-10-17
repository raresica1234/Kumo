package is.rares.kumo.security.mapping;

import is.rares.kumo.core.config.GlobalMapperConfig;
import is.rares.kumo.security.domain.ClientLocation;
import is.rares.kumo.security.model.ClientLocationModel;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface ClientLocationMapping {
    ClientLocation mapModelToEntity(ClientLocationModel model);

    ClientLocationModel mapEntityToModel(ClientLocation entity);
}
