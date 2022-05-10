package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.DimensionsEntity;

import java.time.LocalDateTime;

public record ItemAdvertisementResponse(
        String title,
        String description,
        LocalDateTime createdAt,
        DimensionsEntity dimensions,
        String creatorFirstName,
        String creatorLastName,
        String creatorPhone,
        String creatorCity
) {
}
