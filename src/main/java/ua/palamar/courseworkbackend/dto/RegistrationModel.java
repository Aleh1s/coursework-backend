package ua.palamar.courseworkbackend.dto;

import java.time.LocalDate;

public record RegistrationModel(
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
