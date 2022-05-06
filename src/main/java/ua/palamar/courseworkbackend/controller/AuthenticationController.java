package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.AuthenticationModel;
import ua.palamar.courseworkbackend.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/authentication")
@CrossOrigin("http://localhost:3000")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationModel authenticationModel) {
        return authenticationService.authenticate(authenticationModel);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken) {
        return authenticationService.refresh(refreshToken);
    }
}
