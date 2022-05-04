package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.RegistrationModel;
import ua.palamar.courseworkbackend.service.RegistrationService;

@RestController
@RequestMapping("/api/v1/registration")
@CrossOrigin("http://localhost:3000")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationModel registrationModel) {
        return registrationService.register(registrationModel);
    }

}
