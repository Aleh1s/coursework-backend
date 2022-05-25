package ua.palamar.courseworkbackend.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    @Autowired
    public SimpleAuthenticationService(
            UserService userService,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticationRequestModel authenticationRequestModel) {
        UserEntity currentUser = userService.getUserEntityByEmail(authenticationRequestModel.email());

        if (!passwordEncoder.matches(authenticationRequestModel.password(), currentUser.getPassword())) {
            throw new ApiRequestException("Wrong password");
        }

        String accessToken = tokenProvider.generateToken(currentUser);
        String refreshToken = tokenProvider.generateRefreshToken(currentUser);


        UserResponseModel userResponseModel = getUserResponseModel(currentUser);

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

            UserResponseModel userResponseModel = getUserResponseModel(currentUser);

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

    private UserResponseModel getUserResponseModel(UserEntity userEntity) {
        return new UserResponseModel(
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhoneNumber()
        );
    }
}
