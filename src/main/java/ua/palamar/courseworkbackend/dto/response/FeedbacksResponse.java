package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.feedback.Feedback;

import java.util.Set;

public record FeedbacksResponse(
        Set<Feedback> feedbacks,
        Long count
) {
}
