package is.rares.kumo.controller.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import is.rares.kumo.security.model.ClientLocationModel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Login request object")
public class LoginRequest {
    @NotNull(message = "Username must not be empty")
    @Schema(description = "The account username")
    String username;

    @NotNull(message = "Password must not be empty")
    @Schema(description = "The account password")
    String password;

    @NotNull(message = "Client location missing")
    @Schema(description = "The client's location")
    ClientLocationModel clientLocation;
}
