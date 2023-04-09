package is.rares.kumo.controller.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import is.rares.kumo.security.entity.ClientLocation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Login request object")
public class LoginRequest {
    @NotNull(message = "Username must not be empty")
    @Schema(description = "The account username")
    @Size(min = 6, max = 25, message = "Username needs to be between 6 and 25 characters")
    String username;

    @NotNull(message = "Password must not be empty")
    @Schema(description = "The account password")
    @Size(min = 6, max = 35, message = "Password needs to be between 6 and 35 characters")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z]).*", message = "Password must have at least one uppercase and one lowercase character")
    String password;

    @NotNull(message = "Client location missing")
    @Schema(description = "The client's location")
    ClientLocation clientLocation;
}
