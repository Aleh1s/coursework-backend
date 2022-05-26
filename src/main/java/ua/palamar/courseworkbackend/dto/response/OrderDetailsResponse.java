package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.order.Delivery;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;

import java.time.LocalDateTime;

public record OrderDetailsResponse(
        String uniqueId,
        LocalDateTime createdAt,
        OrderStatus orderStatus,
        Delivery delivery,
        UserResponse receiver,
        String wishes
) {
}
