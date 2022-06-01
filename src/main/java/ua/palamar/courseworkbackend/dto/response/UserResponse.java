package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.user.UserStatus;
import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;

public record UserResponse(
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        UserStatus status,
        UserRole role
) {
}
