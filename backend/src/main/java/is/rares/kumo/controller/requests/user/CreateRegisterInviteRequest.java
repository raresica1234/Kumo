package is.rares.kumo.controller.requests.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Object to create a register invite")
public class CreateRegisterInviteRequest {

    @NotNull
    @Schema(description = "Specifies for how long the invite should be valid, 0 for unlimited")
    @Min(0)
    private long validitySeconds;

    @NotNull
    @Schema(description = "Number of uses, 0 for unlimited")
    @Min(0)
    private int uses;
}
