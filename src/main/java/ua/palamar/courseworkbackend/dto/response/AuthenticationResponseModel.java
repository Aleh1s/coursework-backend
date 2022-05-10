package ua.palamar.courseworkbackend.dto.response;

public record AuthenticationResponseModel(
        String accessToken,
        String refreshToken,
        UserResponseModel userResponseModel
) {

}
