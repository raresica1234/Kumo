package is.rares.kumo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Schema(description = "User data")
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @NotNull
    @Schema(description = "User id")
    UUID uuid;

    @NotNull
    @Schema(description = "Username")
    String username;

    @NotNull
    @Schema(description = "Email")
    String email;

    @NotNull
    @Schema(description = "First name")
    String firstName;

    @NotNull
    @Schema(description = "Last name")
    String lastName;
}
