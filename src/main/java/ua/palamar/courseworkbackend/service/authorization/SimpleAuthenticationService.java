package ua.palamar.courseworkbackend.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.AuthenticationModel;
import ua.palamar.courseworkbackend.dto.AuthenticationResponseModel;
import ua.palamar.courseworkbackend.dto.UserModel;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AuthenticationService;
import ua.palamar.courseworkbackend.service.UserService;

@Service
public class SimpleAuthenticationService implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    public SimpleAuthenticationService(UserService userService,
                                       PasswordEncoder passwordEncoder,
                                       TokenProvider tokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticationModel authenticationModel) {
        UserEntity currentUser = userService.getUserEntityByEmail(authenticationModel.email());

        if (!passwordEncoder.matches(authenticationModel.password(), currentUser.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        String accessToken = tokenProvider.generateToken(currentUser);
        String refreshToken = tokenProvider.generateRefreshToken(currentUser);


        UserModel userModel = new UserModel(
                currentUser.getEmail(),
                currentUser.getUserInfo().getFirstName(),
                currentUser.getUserInfo().getLastName(),
                currentUser.getUserInfo().getCity(),
                currentUser.getUserInfo().getPhoneNumber(),
                currentUser.getAge()
        );

        return new ResponseEntity<>(new AuthenticationResponseModel(
                accessToken,
                refreshToken,
                userModel
        ), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> refresh(String token) {
        String refreshToken = tokenProvider.resolveToken(token);

        if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
            UserEntity currentUser = userService.getUserEntityByEmail(
                    tokenProvider.getEmailByToken(refreshToken)
            );

            String newAccessToken = tokenProvider.generateToken(currentUser);

            UserModel userModel = new UserModel(
                    currentUser.getEmail(),
                    currentUser.getUserInfo().getFirstName(),
                    currentUser.getUserInfo().getLastName(),
                    currentUser.getUserInfo().getCity(),
                    currentUser.getUserInfo().getPhoneNumber(),
                    currentUser.getAge()
            );

            AuthenticationResponseModel authenticationResponseModel = new AuthenticationResponseModel(
                    newAccessToken,
                    refreshToken,
                    userModel
            );

            return new ResponseEntity<>(authenticationResponseModel, HttpStatus.ACCEPTED);
        } else {
            throw new ApiRequestException("Token isn't valid");
        }
    }
}
