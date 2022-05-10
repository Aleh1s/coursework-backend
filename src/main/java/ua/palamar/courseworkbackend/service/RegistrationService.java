package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.request.RegistrationRequestModel;

public interface RegistrationService {

    ResponseEntity<?> register(RegistrationRequestModel registrationRequestModel);

}
