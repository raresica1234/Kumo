package is.rares.kumo.mapping;

import is.rares.kumo.core.config.GlobalMapperConfig;
import is.rares.kumo.domain.user.User;
import is.rares.kumo.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapping {

    @Mapping(source = "accountDetails.firstName", target = "firstName")
    @Mapping(source = "accountDetails.lastName", target = "lastName")
    UserModel mapEntityToModel(User user);
}