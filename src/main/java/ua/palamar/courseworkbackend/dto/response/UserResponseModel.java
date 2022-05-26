package ua.palamar.courseworkbackend.dto.response;

public record UserResponseModel(
        String email,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
