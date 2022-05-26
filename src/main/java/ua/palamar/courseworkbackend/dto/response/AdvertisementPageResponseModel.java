package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;

import java.util.List;

public record AdvertisementPageResponseModel(
        List<Advertisement> advertisements,
        Long totalCount
) {
}
