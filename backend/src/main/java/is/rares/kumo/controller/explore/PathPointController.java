package is.rares.kumo.controller.explore;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import is.rares.kumo.controller.responses.BooleanResponse;
import is.rares.kumo.model.explore.PathPointModel;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasAuthority;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.enums.Feature;
import is.rares.kumo.service.explore.PathPointService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/explore/path_point")
public class PathPointController {

    private final PathPointService pathPointService;

    public PathPointController(PathPointService pathPointService) {
        this.pathPointService = pathPointService;
    }

    @Operation(summary = "Get path points", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate path point"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.GET_PATH_POINT)
    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public List<PathPointModel> getPathPoint(@RequestParam(defaultValue = "") String name,
                                             Pageable pageable) {
        return pathPointService.getPathPoints(name, pageable);
    }

    @Operation(summary = "Create path point", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PathPointModel.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate path point"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.CREATE_PATH_POINT)
    @PostMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public PathPointModel createPathPoint(@Valid @RequestBody PathPointModel pathPointModel) {
        return pathPointService.createPathPoint(pathPointModel);
    }

    @Operation(summary = "Update path point", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PathPointModel.class))),
            @ApiResponse(responseCode = "400", description = "Id missing"),
            @ApiResponse(responseCode = "404", description = "Path point not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate path point"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.UPDATE_PATH_POINT)
    @PutMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public PathPointModel updatePathPoint(@Valid @RequestBody PathPointModel pathPointModel) {
        return pathPointService.updatePathPoint(pathPointModel);
    }

    @Operation(summary = "Delete path point", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PathPointModel.class))),
            @ApiResponse(responseCode = "404", description = "Path point not found"),
    })
    @Authenticated
    @HasTokenType
    @HasAuthority(Feature.DELETE_PATH_POINT)
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON)
    public BooleanResponse deletePathPoint(@PathVariable UUID id) {
        pathPointService.deletePathPoint(id);

        return new BooleanResponse(true);
    }

}
