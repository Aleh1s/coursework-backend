package ua.palamar.courseworkbackend.dto.request;

import java.time.LocalDate;

public record RegistrationRequestModel(
        String email,
        String password,
        String firstName,
        String lastName,
        String city,
        String address,
        String postNumber,
        String phoneNumber,
        LocalDate dob
) {

}
