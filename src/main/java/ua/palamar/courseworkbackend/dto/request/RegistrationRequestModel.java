package ua.palamar.courseworkbackend.dto.request;

public record RegistrationRequestModel(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber
) {

}
