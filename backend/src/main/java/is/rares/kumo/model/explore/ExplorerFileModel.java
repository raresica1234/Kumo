package is.rares.kumo.model.explore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Explorer result file model")
@AllArgsConstructor
@NoArgsConstructor
public class ExplorerFileModel {
    private String name;
    private String fullPath;
    private FileTypeModel type;

    @NotNull
    private boolean write;

    @NotNull
    private boolean delete;

    @NotNull
    private boolean modifyRoot;
}
