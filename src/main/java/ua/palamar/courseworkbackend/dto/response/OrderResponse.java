package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.Delivery;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponse(
        String uniqueId,
        LocalDateTime createdAt,
        OrderStatus orderStatus,
        Delivery delivery,
        Advertisement product,
        UserResponse sender,
        String wishes
) {
}
