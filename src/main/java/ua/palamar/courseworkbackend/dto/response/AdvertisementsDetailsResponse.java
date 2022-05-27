package ua.palamar.courseworkbackend.dto.response;

import java.util.List;

public record AdvertisementsDetailsResponse(
        List<AdvertisementResponse> advertisements,
        Long totalCount
) {
}
