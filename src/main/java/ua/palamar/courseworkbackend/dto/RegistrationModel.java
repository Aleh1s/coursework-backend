package ua.palamar.courseworkbackend.dto;

public record RegistrationModel(
        String email,
        String password,
        String firstName,
        String lastName,
        String city,
        String address,
        String postNumber,
        String phoneNumber
) {}
