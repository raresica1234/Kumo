package is.rares.kumo.model.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import is.rares.kumo.security.entity.ClientLocation;
import is.rares.kumo.security.enums.ClientLocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "Specifies a client location")
@AllArgsConstructor
@NoArgsConstructor
public class ClientLocationModel {

    @NotNull
    @ApiModelProperty(notes = "Client country")
    String country;

    @NotNull
    @ApiModelProperty(notes = "Client ip address")
    String ipAddress;

    @NotNull
    @ApiModelProperty(notes = "Client location type")
    ClientLocationType locationType;

    public ClientLocationModel(ClientLocation clientLocation) {
        this.country = clientLocation.getCountry();
        this.ipAddress = clientLocation.getIpAddress();
        this.locationType = clientLocation.getLocationType();
    }

}
