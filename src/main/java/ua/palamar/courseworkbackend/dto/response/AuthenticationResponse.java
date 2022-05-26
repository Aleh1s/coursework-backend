package ua.palamar.courseworkbackend.dto.response;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        UserResponse userResponse
) {

}
