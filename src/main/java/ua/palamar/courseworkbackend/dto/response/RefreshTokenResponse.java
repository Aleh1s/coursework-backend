package ua.palamar.courseworkbackend.dto.response;

public record RefreshTokenResponse(
        String token,
        UserResponse userResponse
) {
}
