package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;

import java.util.List;

public record AdvertisementsResponse(
        List<Advertisement> advertisements,
        Long totalCount
) {
}
