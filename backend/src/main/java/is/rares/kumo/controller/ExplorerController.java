package is.rares.kumo.controller;

import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.enums.TokenType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/explore")
public class ExplorerController {

    @GetMapping()
    @Authenticated
    @HasTokenType
    public ResponseEntity<String> explore() {
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
