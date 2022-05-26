package ua.palamar.courseworkbackend.dto.response;

public record FeedbackResponse(
        String id,
        String email,
        String text
) {
}
