package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.RegistrationModel;

public interface RegistrationService {

    ResponseEntity<?> register(RegistrationModel registrationModel);

}
