package is.rares.kumo.controller.responses;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
public class TokenDataResponse {
    @NotNull
    @ApiModelProperty(notes = "The jwt")
    private String jwtToken;

    @NotNull
    @ApiModelProperty(notes = "The refresh token")
    private String refreshToken;

    @NotNull
    @ApiModelProperty(notes = "THe validity of the access token")
    private int validityMs;
}
