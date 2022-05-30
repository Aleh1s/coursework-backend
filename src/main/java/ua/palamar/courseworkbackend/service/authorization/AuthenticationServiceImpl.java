package ua.palamar.courseworkbackend.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.request.AuthenticationRequest;
import ua.palamar.courseworkbackend.dto.response.AuthenticationResponse;
import ua.palamar.courseworkbackend.dto.response.RefreshTokenResponse;
import ua.palamar.courseworkbackend.dto.response.UserAuthResponse;
import ua.palamar.courseworkbackend.dto.response.UserResponse;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.entity.user.UserStatus;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AuthenticationService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthenticationServiceImpl(
            UserService userService,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        UserAccount currentUser = userService.getUserEntityByEmail(authenticationRequest.email());

        if (currentUser.getStatus().equals(UserStatus.BLOCKED)) {
            throw new ApiRequestException("User is blocked");
        }

        if (!passwordEncoder.matches(authenticationRequest.password(), currentUser.getPassword())) {
            throw new ApiRequestException("Wrong password");
        }

        String accessToken = tokenProvider.generateToken(currentUser);
        String refreshToken = tokenProvider.generateRefreshToken(currentUser);

        UserAuthResponse response = new UserAuthResponse(
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getPhoneNumber(),
                currentUser.getRole()
        );

        return new AuthenticationResponse(
                accessToken,
                refreshToken,
                response
        );
    }

    @Override
    public RefreshTokenResponse refresh(HttpServletRequest request) {
        String refreshToken = tokenProvider.resolveToken(request);

        if (Objects.nonNull(refreshToken) && tokenProvider.validateToken(refreshToken)) {
            UserAccount currentUser = userService.getUserEntityByEmail(
                    tokenProvider.getEmailByToken(refreshToken)
            );

            if (currentUser.getStatus().equals(UserStatus.BLOCKED)) {
                throw new ApiRequestException("User is blocked");
            }

            String token = tokenProvider.generateToken(currentUser);

            UserAuthResponse response = new UserAuthResponse(
                    currentUser.getEmail(),
                    currentUser.getFirstName(),
                    currentUser.getLastName(),
                    currentUser.getPhoneNumber(),
                    currentUser.getRole()
            );

            return new RefreshTokenResponse(
                    token,
                    response
            );

        } else {
            throw new ApiRequestException("Token isn't valid");
        }
    }
}
