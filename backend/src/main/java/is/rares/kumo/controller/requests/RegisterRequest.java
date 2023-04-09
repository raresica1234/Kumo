package is.rares.kumo.controller.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Register request object")
public class RegisterRequest {
    @NotNull
    @Schema(description = "The account username")
    @Size(min = 6, max = 25, message = "Username needs to be between 6 and 25 characters")
    String username;

    @NotNull
    @Schema(description = "The account email")
    @Email
    @NotBlank(message = "Email can not be empty")
    String email;

    @NotNull
    @Schema(description = "The account password")
    @Size(min = 6, max = 35, message = "Password needs to be between 6 and 35 characters")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z]).*", message = "Password must have at least one lowercase and one uppercase character")
    String password;

}
