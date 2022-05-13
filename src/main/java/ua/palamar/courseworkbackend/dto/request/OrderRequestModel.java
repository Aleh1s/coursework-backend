package ua.palamar.courseworkbackend.dto.request;

import java.util.Optional;

public record OrderRequestModel(
        String advertisementId,
        Optional<String> city,
        Optional<String> address,
        Optional<String> postNumber,
        Optional<String> wishes,
        String advertisementCategory
) {
}
