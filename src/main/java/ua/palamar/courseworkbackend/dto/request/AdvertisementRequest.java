package ua.palamar.courseworkbackend.dto.request;

import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementCategory;

public record AdvertisementRequest(
        String title,
        String description,
        AdvertisementCategory category,
        String city
) {

}
