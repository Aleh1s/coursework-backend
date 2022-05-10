package ua.palamar.courseworkbackend.dto.request;

import java.util.Optional;

public record AdvertisementRequestModel(
        String title,
        String description,
        String category,
        Optional<Integer> height,
        Optional<Integer> length,
        Optional<Integer> width
) {

}
