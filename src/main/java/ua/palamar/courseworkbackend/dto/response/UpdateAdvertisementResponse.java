package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementCategory;

public record UpdateAdvertisementResponse(
        String title,
        String description,
        String city,
        AdvertisementCategory category
) {
}
