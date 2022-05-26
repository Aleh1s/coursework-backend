package ua.palamar.courseworkbackend.dto.request;

public record DeliveryRequestModel(
        String city,
        String address,
        String postOffice
) {
}
