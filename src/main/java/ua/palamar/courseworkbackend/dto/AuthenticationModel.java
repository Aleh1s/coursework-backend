package ua.palamar.courseworkbackend.dto;

public record AuthenticationModel(
        String email,
        String password
) {
}
