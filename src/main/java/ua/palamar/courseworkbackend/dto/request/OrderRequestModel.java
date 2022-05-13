package ua.palamar.courseworkbackend.dto.request;

public record OrderRequestModel(
        String advertisementId,
        String firstName,
        String lastName,
        String city,
        String phoneNumber,
        String address,
        String postNumber,
        String wishes,
        String advertisementCategory
) {
}
