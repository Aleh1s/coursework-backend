package ua.palamar.courseworkbackend.dto.request;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String phoneNumber
) {
}
