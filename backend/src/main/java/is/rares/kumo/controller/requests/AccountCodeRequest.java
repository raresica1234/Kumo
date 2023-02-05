package is.rares.kumo.controller.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import is.rares.kumo.security.entity.ClientLocation;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "Login request object for account code validation")
public class AccountCodeRequest {
    @NotNull
    @ApiModelProperty(notes = "The 2FA code")
    String code;

    @NotNull
    @ApiModelProperty(notes = "The client's location")
    ClientLocation clientLocation;
}
