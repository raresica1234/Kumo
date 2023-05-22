package is.rares.kumo.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import is.rares.kumo.security.domain.ClientLocation;
import is.rares.kumo.security.enums.ClientLocationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Specifies a client location")
@AllArgsConstructor
@NoArgsConstructor
public class ClientLocationModel {

    @NotNull
    @Schema(description = "Client country")
    String country;

    @NotNull
    @Schema(description = "Client ip address")
    String ipAddress;

    @NotNull
    @Schema(description = "Client location type")
    ClientLocationType locationType;

    public ClientLocationModel(ClientLocation clientLocation) {
        this.country = clientLocation.getCountry();
        this.ipAddress = clientLocation.getIpAddress();
        this.locationType = clientLocation.getLocationType();
    }

}
