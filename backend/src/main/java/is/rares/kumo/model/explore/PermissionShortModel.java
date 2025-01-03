package is.rares.kumo.model.explore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Schema(description = "Permission Short Model")
@AllArgsConstructor
@NoArgsConstructor
public class PermissionShortModel {
    private UUID uuid;

    @NotNull
    private UUID pathPointId;

    @NotNull
    private UUID explorationRoleId;

    @NotNull
    private boolean read;

    @NotNull
    private boolean write;

    @NotNull
    private boolean delete;

    @NotNull
    private boolean modifyRoot;
}
