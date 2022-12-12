package is.rares.kumo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/authenticate")
public class AuthenticationController {

    @GetMapping(value = "/login")
    public ResponseEntity login() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
