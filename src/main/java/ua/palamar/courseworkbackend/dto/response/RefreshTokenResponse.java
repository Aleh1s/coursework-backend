package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.dto.request.UserDto;

public record RefreshTokenResponse(
        String token,
        UserResponseModel userResponseModel
) {
}
