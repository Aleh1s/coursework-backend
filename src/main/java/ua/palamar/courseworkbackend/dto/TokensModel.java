package ua.palamar.courseworkbackend.dto;

public record TokensModel(
        String accessToken,
        String refreshToken
) {
}
