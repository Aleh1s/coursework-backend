package ua.palamar.courseworkbackend.dto.response;

import java.time.LocalDateTime;

public record AdvertisementResponse(
        String title,
        String description,
        LocalDateTime createdAt,
        UserResponseModel userResponseModel
) {
}
