package is.rares.kumo.model.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@ApiModel(description = "Register request object")
public class RegisterRequest {
    @NotNull
    @ApiModelProperty(notes = "The account username")
    @Size(min = 6, max = 25, message = "Username needs to be between 6 and 25 characters")
    String username;

    @NotNull
    @ApiModelProperty(notes = "The account email")
    @Email
    @NotBlank(message = "Email can not be empty")
    String email;

    @NotNull
    @ApiModelProperty(notes = "The account password")
    @Size(min = 6, max = 35, message = "Password needs to be between 6 and 35 characters")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z]).*", message = "Password must have at least one lowercase and one uppercase character")
    String password;

}
