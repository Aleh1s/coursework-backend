package ua.palamar.courseworkbackend.dto;

public record UserModel(
        String email,
        String firstName,
        String lastName,
        String city,
        String phoneNumber,
        int age
) {
}
