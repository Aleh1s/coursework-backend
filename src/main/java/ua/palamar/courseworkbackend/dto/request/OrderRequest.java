package ua.palamar.courseworkbackend.dto.request;

public record OrderRequest(
        String advertisementId,
        String city,
        String address,
        String postOffice,
        String wishes
) {
}
