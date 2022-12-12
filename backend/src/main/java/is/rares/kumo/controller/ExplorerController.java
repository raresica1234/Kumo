package is.rares.kumo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/explore")
public class ExplorerController {

    @GetMapping("/")
    public ResponseEntity explore() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
