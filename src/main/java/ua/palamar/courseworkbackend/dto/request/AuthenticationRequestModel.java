package ua.palamar.courseworkbackend.dto.request;

public record AuthenticationRequestModel(
        String email,
        String password
) {
}
