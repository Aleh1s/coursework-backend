package ua.palamar.courseworkbackend.dto.response;

public record UserResponse(
        String email,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
