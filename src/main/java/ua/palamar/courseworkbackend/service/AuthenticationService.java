package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.request.AuthenticationRequestModel;
import ua.palamar.courseworkbackend.dto.response.AuthenticationResponseModel;
import ua.palamar.courseworkbackend.dto.response.RefreshTokenResponse;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    AuthenticationResponseModel authenticate(AuthenticationRequestModel authenticationRequestModel);

    RefreshTokenResponse refresh(HttpServletRequest request);
}
