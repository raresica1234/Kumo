package is.rares.kumo.controller.explore;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.rares.kumo.controller.requests.explore.SetUserExplorationRolesRequest;
import is.rares.kumo.controller.responses.BooleanResponse;
import is.rares.kumo.model.explore.ExplorationRoleModel;
import is.rares.kumo.model.explore.ExplorationUserRoleModel;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasAuthority;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.enums.Feature;
import is.rares.kumo.service.explore.ExplorationUserRoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.UUID;

@RestController
@RequestMapping("api/explore/userRole")
@AllArgsConstructor
public class ExplorationUserRoleController {
    private final ExplorationUserRoleService explorationUserRoleService;

    @Operation(summary = "Get users with exploration roles", responses = {
            @ApiResponse(responseCode = "200"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.GET_USER_EXPLORATION_ROLE)
    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public Page<ExplorationUserRoleModel> getUserExplorationRole(@RequestParam(defaultValue = "") String username,
                                                                 @RequestParam(defaultValue = "") String roleName,
                                                                 Pageable pageable) {
        return explorationUserRoleService.get(username, roleName, pageable);
    }

    @Operation(summary = "Set user exploration roles", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema =
            @Schema(implementation = SetUserExplorationRolesRequest.class))),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.SET_USER_EXPLORATION_ROLES)
    @PostMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ExplorationUserRoleModel setUserExplorationRoles(@Valid @RequestBody SetUserExplorationRolesRequest model) {
        return explorationUserRoleService.setUserExplorationRoles(model);

    }
    @Operation(summary = "Unassign role from user", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BooleanResponse.class))),
            @ApiResponse(responseCode = "404", description = "User does not have the role"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.UNASSIGN_USER_EXPLORATION_ROLE)
    @DeleteMapping(value = "{user}/{role}", produces = MediaType.APPLICATION_JSON)
    public BooleanResponse unassignUserExplorationRole(@PathVariable UUID user, @PathVariable UUID role) {
        explorationUserRoleService.unassignUserExplorationRole(user, role);

        return new BooleanResponse(true);
    }
}