package is.rares.kumo.model.explore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Exploration User Role")
@AllArgsConstructor
@NoArgsConstructor
public class ExplorationUserRoleModel {
    private UUID uuid;
    private String username;

    private List<ExplorationRoleModel> roles;
}
