package ua.palamar.courseworkbackend.dto.criteria;

public record OrderCriteria(
        Integer limit,
        Integer page,
        String sortBy
) {
}
