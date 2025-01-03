package is.rares.kumo.controller.explore;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.rares.kumo.controller.responses.BooleanResponse;
import is.rares.kumo.model.explore.ExplorationRoleModel;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasAuthority;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.enums.Feature;
import is.rares.kumo.service.explore.ExplorationRoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.UUID;

@RestController
@RequestMapping("api/explore/role")
@AllArgsConstructor
public class ExplorationRoleController {

    private final ExplorationRoleService explorationRoleService;

    @Operation(summary = "Get exploration roles", responses = {
            @ApiResponse(responseCode = "200"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.GET_EXPLORATION_ROLE)
    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public Page<ExplorationRoleModel> getExplorationRoles(@RequestParam(defaultValue = "") String name,
                                                          Pageable pageable) {
        return explorationRoleService.get(name, pageable);
    }

    @Operation(summary = "Create exploration role", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema =
            @Schema(implementation = ExplorationRoleModel.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate exploration role"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.CREATE_EXPLORATION_ROLE)
    @PostMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ExplorationRoleModel createExplorationRole(@Valid @RequestBody ExplorationRoleModel model) {
        return explorationRoleService.create(model);
    }
    
    @Operation(summary = "Update exploration role", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ExplorationRoleModel.class))),
            @ApiResponse(responseCode = "400", description = "Id missing"),
            @ApiResponse(responseCode = "404", description = "Exploration role not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate exploration role"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.UPDATE_EXPLORATION_ROLE)
    @PutMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ExplorationRoleModel updateExplorationRole(@Valid @RequestBody ExplorationRoleModel model) {
        return explorationRoleService.update(model);
    }
    
    @Operation(summary = "Delete exploration role", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BooleanResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exploration role not found"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.DELETE_EXPLORATION_ROLE)
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON)
    public BooleanResponse deleteExplorationRole(@PathVariable UUID id) {
        explorationRoleService.delete(id);

        return new BooleanResponse(true);
    }
}
