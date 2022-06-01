package ua.palamar.courseworkbackend.dto.response;

import org.springframework.data.domain.Page;
import ua.palamar.courseworkbackend.entity.feedback.Feedback;

import java.util.List;

public record FeedbacksResponse(
        List<FeedbackResponse> feedbacks,
        Long count
) {
}
