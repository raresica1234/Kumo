package is.rares.kumo.controller.responses.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
public class TokenDataResponse {
    @NotNull
    @Schema(description = "The jwt")
    private String jwtToken;

    @NotNull
    @Schema(description = "The refresh token")
    private String refreshToken;

    @NotNull
    @Schema(description = "THe validity of the access token")
    private int validityMs;
}
