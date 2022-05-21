package ua.palamar.courseworkbackend.dto.request;

public record OrderRequestModel(
        String advertisementId,
        String city,
        String address,
        String postOffice,
        String wishes
) {
}
