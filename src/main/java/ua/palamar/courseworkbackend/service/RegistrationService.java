package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.request.RegistrationRequest;

public interface RegistrationService {

    ResponseEntity<?> register(RegistrationRequest registrationRequest);

}
