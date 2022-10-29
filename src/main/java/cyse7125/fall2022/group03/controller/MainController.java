package cyse7125.fall2022.group03.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    @GetMapping("/healthz")
    public ResponseEntity<String> getUsers()
    {
        return ResponseEntity.status(HttpStatus.OK).body("");

    }
}
