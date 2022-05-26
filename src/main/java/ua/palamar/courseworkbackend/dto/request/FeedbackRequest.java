package ua.palamar.courseworkbackend.dto.request;

public record FeedbackRequest(
        String email,
        String text
) {
}
