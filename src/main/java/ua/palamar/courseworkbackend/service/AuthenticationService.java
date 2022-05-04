package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.AuthenticationModel;

public interface AuthenticationService {

    ResponseEntity<?> authenticate(AuthenticationModel authenticationModel);
}
