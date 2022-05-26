package ua.palamar.courseworkbackend.service;

import ua.palamar.courseworkbackend.dto.request.AuthenticationRequest;
import ua.palamar.courseworkbackend.dto.response.AuthenticationResponse;
import ua.palamar.courseworkbackend.dto.response.RefreshTokenResponse;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    RefreshTokenResponse refresh(HttpServletRequest request);
}
