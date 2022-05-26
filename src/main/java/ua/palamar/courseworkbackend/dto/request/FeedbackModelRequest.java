package ua.palamar.courseworkbackend.dto.request;

public record FeedbackModelRequest(
        String email,
        String text
) {
}
