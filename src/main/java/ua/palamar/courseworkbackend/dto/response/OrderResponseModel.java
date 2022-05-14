package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.DeliveryEntity;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponseModel(
        String uniqueId,
        LocalDateTime createdAt,
        OrderStatus orderStatus,
        DeliveryEntity deliveryEntity,
        Advertisement product,
        UserResponseModel sender,
        String wishes
) {
}
