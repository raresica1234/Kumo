package is.rares.kumo.security.convertor;

import is.rares.kumo.security.domain.ClientLocation;
import is.rares.kumo.security.model.ClientLocationModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClientLocationConvertor {
    private final ModelMapper modelMapper;

    public ClientLocationConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ClientLocation mapModelToEntity(ClientLocationModel model) {
        return modelMapper.map(model, ClientLocation.class);
    }

    public ClientLocationModel mapEntityToModel(ClientLocation entity) {
        return modelMapper.map(entity, ClientLocationModel.class);
    }
}
