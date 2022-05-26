package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Category;

import java.time.LocalDateTime;

public record AdvertisementResponse(
        String uniqueId,
        String title,
        String description,
        String city,
        Category category,
        LocalDateTime createdAt,
        UserResponse userResponse
) {
}
