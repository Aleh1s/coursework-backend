package ua.palamar.courseworkbackend.dto;

public record AuthenticationResponseModel(
        String accessToken,
        String refreshToken,
        UserModel userModel
) {

}
