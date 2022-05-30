package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;

public record UserAuthResponse(
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        UserRole role
) {
}
