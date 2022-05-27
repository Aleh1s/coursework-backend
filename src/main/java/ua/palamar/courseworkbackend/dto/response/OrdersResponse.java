package ua.palamar.courseworkbackend.dto.response;

import java.util.List;

public record OrdersResponse(
        List<OrderResponse> orders,
        Long totalCount
) {
}
