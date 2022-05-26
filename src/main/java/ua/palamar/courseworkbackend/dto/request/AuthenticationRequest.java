package ua.palamar.courseworkbackend.dto.request;

public record AuthenticationRequest(
        String email,
        String password
) {
}
