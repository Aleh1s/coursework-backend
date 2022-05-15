package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Category;

import java.time.LocalDateTime;

public record AdvertisementResponse(
        String uniqueId,
        String title,
        String description,
        Category category,
        LocalDateTime createdAt,
        UserResponseModel userResponseModel
) {
}
