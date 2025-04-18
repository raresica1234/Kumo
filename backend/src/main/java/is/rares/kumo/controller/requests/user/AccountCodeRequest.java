package is.rares.kumo.controller.requests.user;

import io.swagger.v3.oas.annotations.media.Schema;
import is.rares.kumo.security.model.ClientLocationModel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Login request object for account code validation")
public class AccountCodeRequest {
    @NotNull
    @Schema(description = "The 2FA code")
    String code;

    @NotNull
    @Schema(description = "The client's location")
    ClientLocationModel clientLocation;
}
