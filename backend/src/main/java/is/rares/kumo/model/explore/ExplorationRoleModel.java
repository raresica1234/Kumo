package is.rares.kumo.model.explore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Schema(description = "Exploration Role")
@AllArgsConstructor
@NoArgsConstructor
public class ExplorationRoleModel {
    private UUID uuid;

    @NotNull
    private String name;
}
