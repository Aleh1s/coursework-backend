package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementCategory;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;

import java.time.LocalDateTime;

public record AdvertisementResponse(
        String uniqueId,
        String title,
        String description,
        String city,
        AdvertisementStatus status,
        AdvertisementCategory category,
        LocalDateTime createdAt,
        UserResponse userResponse
) {
}
