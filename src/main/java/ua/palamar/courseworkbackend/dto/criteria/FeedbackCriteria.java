package ua.palamar.courseworkbackend.dto.criteria;

public record FeedbackCriteria(
        Integer limit,
        Integer page,
        String sortBy
) {
}
