package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.AuthenticationModel;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    ResponseEntity<?> authenticate(AuthenticationModel authenticationModel);

    ResponseEntity<?> refresh(String refreshToken);
}
