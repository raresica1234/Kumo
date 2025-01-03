package is.rares.kumo.controller.explore;

import is.rares.kumo.model.explore.ExplorerFileModel;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.services.CurrentUserService;
import is.rares.kumo.service.explore.ExplorerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping(path = "/api/explore")
@AllArgsConstructor
public class ExplorerController {
    private final CurrentUserService currentUserService;
    private final ExplorerService explorerService;

    @Authenticated
    @HasTokenType
    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public List<ExplorerFileModel> explore(@RequestParam(defaultValue = "") String path) {
        var currentUser = currentUserService.getUser();
        return explorerService.explore(path, currentUser);
    }

}
