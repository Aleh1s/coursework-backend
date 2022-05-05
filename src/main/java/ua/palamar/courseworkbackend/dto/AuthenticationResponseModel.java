package ua.palamar.courseworkbackend.dto;

import ua.palamar.courseworkbackend.entity.UserEntity;

public record AuthenticationResponseModel(
        String accessToken,
        String refreshToken,
        UserModel userModel
) {

}
