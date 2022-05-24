package ua.palamar.courseworkbackend.dto.request;

public record UserDto(
        String firstName,
        String lastName,
        String phoneNumber
) {
}
