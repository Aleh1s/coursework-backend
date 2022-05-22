package ua.palamar.courseworkbackend.dto.request;

import ua.palamar.courseworkbackend.entity.advertisement.Category;

public record AdvertisementRequestModel(
        String title,
        String description,
        Category category,
        String city
) {

}
