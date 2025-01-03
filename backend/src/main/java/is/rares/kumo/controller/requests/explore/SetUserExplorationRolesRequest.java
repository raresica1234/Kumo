package is.rares.kumo.controller.requests.explore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Set user exploration roles request")
public class SetUserExplorationRolesRequest {
    @NotNull
    @Schema(description = "User id")
    UUID uuid;

    @NotNull
    @Schema(description = "List of exploration role ids")
    List<UUID> explorationRoles;
}
