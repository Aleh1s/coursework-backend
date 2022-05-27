package ua.palamar.courseworkbackend.dto.response;

import java.util.List;

public record OrdersDetailsResponse(
        List<OrderDetailsResponse> orderDetails,
        Long totalCount
) {
}
