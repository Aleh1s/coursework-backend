package ua.palamar.courseworkbackend.dto.criteria;

import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementCategory;

public record AdvertisementCriteria(
        AdvertisementCategory category,
        Integer limit,
        Integer page,
        String sortBy,
        String query
) {
}
