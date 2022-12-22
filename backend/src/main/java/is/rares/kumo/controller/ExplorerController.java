package is.rares.kumo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/explore")
public class ExplorerController {

    @GetMapping()
    public ResponseEntity<String> explore() {
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
