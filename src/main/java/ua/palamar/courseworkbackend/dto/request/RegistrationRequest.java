package ua.palamar.courseworkbackend.dto.request;

public record RegistrationRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber
) {

}
