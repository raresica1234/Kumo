package is.rares.kumo.model.explore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Schema(description = "Path point")
@AllArgsConstructor
@NoArgsConstructor
public class PathPointModel {
    private UUID uuid;

    @NotNull
    private String path;

    @NotNull
    private boolean root;
}
