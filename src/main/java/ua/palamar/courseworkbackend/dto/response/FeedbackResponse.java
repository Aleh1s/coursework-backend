package ua.palamar.courseworkbackend.dto.response;

import java.time.LocalDateTime;

public record FeedbackResponse(
        String id,
        String email,
        String text,
        LocalDateTime createdAt
) {
}
