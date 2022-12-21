package is.rares.kumo.model.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import is.rares.kumo.security.entity.ClientLocation;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "Login request object")
public class LoginRequest {
    @NotNull
    @ApiModelProperty(notes = "The account username")
    @Size(min = 6, max = 25)
    String username;

    @NotNull
    @ApiModelProperty(notes = "The account password")
    @Size(min = 6, max = 35)
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z]).*")
    String password;

    @NotNull
    @ApiModelProperty(notes = "The client's location")
    ClientLocation clientLocation;
}
