package ua.palamar.courseworkbackend.dto;

public record FeedbackCriteria(
        Integer limit,
        Integer page,
        String sortBy
) {
}
