package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.request.AuthenticationRequestModel;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    ResponseEntity<?> authenticate(AuthenticationRequestModel authenticationRequestModel);

    ResponseEntity<?> refresh(HttpServletRequest request);
}
