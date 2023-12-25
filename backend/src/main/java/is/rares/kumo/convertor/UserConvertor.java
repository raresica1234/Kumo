package is.rares.kumo.convertor;

import is.rares.kumo.domain.user.User;
import is.rares.kumo.model.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor {
    private final ModelMapper modelMapper;


    public UserConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserModel mapEntityToModel(User user) {
        UserModel model = modelMapper.map(user, UserModel.class);

        model.setFirstName(user.getAccountDetails().getFirstName());
        model.setLastName(user.getAccountDetails().getLastName());

        return model;
    }
}
