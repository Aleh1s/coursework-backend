package ua.palamar.courseworkbackend.dto;

import ua.palamar.courseworkbackend.entity.advertisement.Category;

public record AdvertisementCriteria(
        Category category,
        Integer limit,
        Integer page,
        String sortBy,
        String query
) {
}
