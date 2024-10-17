package is.rares.kumo.controller.explore;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.rares.kumo.controller.responses.BooleanResponse;
import is.rares.kumo.model.explore.PermissionModel;
import is.rares.kumo.model.explore.PermissionShortModel;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasAuthority;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.enums.Feature;
import is.rares.kumo.service.explore.PermissionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.UUID;

@RestController
@RequestMapping("/api/explore/permission")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(summary = "Get permissions", responses = {
            @ApiResponse(responseCode = "200"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.GET_EXPLORATION_PERMISSION)
    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public Page<PermissionModel> getPermissions(@RequestParam(defaultValue = "") String path,
                                                @RequestParam(defaultValue = "") String role, Pageable pageable) {
        return permissionService.get(path, role, pageable);
    }

    @Operation(summary = "Create Permission", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema =
            @Schema(implementation = PermissionShortModel.class))),
            @ApiResponse(responseCode = "404", description = "Path point or exploration role not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate permission"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.CREATE_EXPLORATION_PERMISSION)
    @PostMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public PermissionShortModel createPermission(@Valid @RequestBody PermissionShortModel model) {
        return permissionService.create(model);
    }

    @Operation(summary = "Update Permission", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema =
            @Schema(implementation = PermissionShortModel.class))),
            @ApiResponse(responseCode = "400", description = "Id missing"),
            @ApiResponse(responseCode = "404", description = "Permission, path point or exploration role not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate permission"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.UPDATE_EXPLORATION_PERMISSION)
    @PutMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public PermissionShortModel updatePermission(@Valid @RequestBody PermissionShortModel model) {
        return permissionService.update(model);
    }
    
    @Operation(summary = "Delete permission", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BooleanResponse.class))),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.DELETE_EXPLORATION_PERMISSION)
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON)
    public BooleanResponse deletePermission(@PathVariable UUID id) {
        permissionService.delete(id);

        return new BooleanResponse(true);
    }
}
