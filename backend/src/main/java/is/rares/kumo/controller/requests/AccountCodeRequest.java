package is.rares.kumo.controller.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import is.rares.kumo.security.model.ClientLocationModel;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@ApiModel(description = "Login request object for account code validation")
public class AccountCodeRequest {
    @NotNull
    @ApiModelProperty(notes = "The 2FA code")
    String code;

    @NotNull
    @ApiModelProperty(notes = "The client's location")
    ClientLocationModel clientLocation;
}
