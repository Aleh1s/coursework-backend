package ua.palamar.courseworkbackend.dto.request;

import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementCategory;

public record UpdateAdvertisementRequest(
        String id,
        String title,
        String description,
        String city,
        AdvertisementCategory category
) {
}
