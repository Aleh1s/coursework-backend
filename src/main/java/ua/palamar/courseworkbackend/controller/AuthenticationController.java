package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.request.AuthenticationRequest;
import ua.palamar.courseworkbackend.dto.response.AuthenticationResponse;
import ua.palamar.courseworkbackend.service.AuthenticationService;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        return new ResponseEntity<>(
                authenticationService.authenticate(authenticationRequest), HttpStatus.OK
        );
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        return new ResponseEntity<>(
                authenticationService.refresh(request), HttpStatus.OK
        );
    }
}
