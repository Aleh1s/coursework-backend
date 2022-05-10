package ua.palamar.courseworkbackend.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.adapter.AuthenticationServiceAdapter;
import ua.palamar.courseworkbackend.dto.request.AuthenticationRequestModel;
import ua.palamar.courseworkbackend.dto.response.AuthenticationResponseModel;
import ua.palamar.courseworkbackend.dto.response.UserResponseModel;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AuthenticationService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Service
public class SimpleAuthenticationService implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationServiceAdapter authenticationServiceAdapter;

    @Autowired
    public SimpleAuthenticationService(UserService userService,
                                       PasswordEncoder passwordEncoder,
                                       TokenProvider tokenProvider,
                                       AuthenticationServiceAdapter authenticationServiceAdapter) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationServiceAdapter = authenticationServiceAdapter;
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticationRequestModel authenticationRequestModel) {
        UserEntity currentUser = userService.getUserEntityByEmail(authenticationRequestModel.email());

        if (!passwordEncoder.matches(authenticationRequestModel.password(), currentUser.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        String accessToken = tokenProvider.generateToken(currentUser);
        String refreshToken = tokenProvider.generateRefreshToken(currentUser);


        UserResponseModel userResponseModel
                = authenticationServiceAdapter.getUserModel(currentUser);

        return new ResponseEntity<>(new AuthenticationResponseModel(
                accessToken,
                refreshToken,
                userResponseModel
        ), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String refreshToken = tokenProvider.resolveToken(request);

        if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
            UserEntity currentUser = userService.getUserEntityByEmail(
                    tokenProvider.getEmailByToken(refreshToken)
            );

            String newAccessToken = tokenProvider.generateToken(currentUser);

            UserResponseModel userResponseModel
                    = authenticationServiceAdapter.getUserModel(currentUser);

            AuthenticationResponseModel authenticationResponseModel = new AuthenticationResponseModel(
                    newAccessToken,
                    refreshToken,
                    userResponseModel
            );

            return new ResponseEntity<>(authenticationResponseModel, HttpStatus.ACCEPTED);
        } else {

            throw new ApiRequestException("Token isn't valid");
        }
    }
}
