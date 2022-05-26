package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.feedback.FeedbackEntity;

import java.util.Set;

public record FeedbacksResponse(
        Set<FeedbackEntity> feedbacks,
        Long count
) {
}
