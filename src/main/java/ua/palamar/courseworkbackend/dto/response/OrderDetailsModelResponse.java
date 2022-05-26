package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.order.DeliveryEntity;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;

import java.time.LocalDateTime;

public record OrderDetailsModelResponse(
        String uniqueId,
        LocalDateTime createdAt,
        OrderStatus orderStatus,
        DeliveryEntity deliveryEntity,
        UserResponseModel receiver,
        String wishes
) {
}
