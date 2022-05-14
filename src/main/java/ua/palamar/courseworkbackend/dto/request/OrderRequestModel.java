package ua.palamar.courseworkbackend.dto.request;

public record OrderRequestModel(
        String advertisementId,
        String deliveryCity,
        String deliveryAddress,
        String deliveryPostOffice,
        String wishes
) {
}
