package ua.palamar.courseworkbackend.service.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.adapter.user.UserDtoAdapter;
import ua.palamar.courseworkbackend.dto.request.AuthenticationRequest;
import ua.palamar.courseworkbackend.dto.response.AuthenticationResponse;
import ua.palamar.courseworkbackend.dto.response.RefreshTokenResponse;
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
    private final UserDtoAdapter userDtoAdapter;

    @Autowired
    public AuthenticationServiceImpl(
            UserService userService,
            PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider,
            UserDtoAdapter userDtoAdapter
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userDtoAdapter = userDtoAdapter;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        String email = authenticationRequest.email().trim();
        String password = authenticationRequest.password().trim();

        UserAccount currentUser = userService.getUserEntityByEmail(email);

        if (currentUser.getStatus().equals(UserStatus.BLOCKED)) {
            throw new ApiRequestException("User is blocked");
        }

        if (!passwordEncoder.matches(password, currentUser.getPassword())) {
            throw new ApiRequestException("Wrong password");
        }

        String accessToken = tokenProvider.generateToken(currentUser);
        String refreshToken = tokenProvider.generateRefreshToken(currentUser);

        UserResponse response = userDtoAdapter.getModel(currentUser);

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

            UserResponse response = userDtoAdapter.getModel(currentUser);

            return new RefreshTokenResponse(
                    token,
                    response
            );

        } else {
            throw new ApiRequestException("Token isn't valid");
        }
    }
}
