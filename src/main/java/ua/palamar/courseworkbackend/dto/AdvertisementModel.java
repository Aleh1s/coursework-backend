package ua.palamar.courseworkbackend.dto;

import java.util.Optional;

public record AdvertisementModel(
        String title,
        String description,
        String category,
        Optional<Integer> height,
        Optional<Integer> length,
        Optional<Integer> width
) {

}
