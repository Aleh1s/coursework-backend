package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;

import java.time.LocalDateTime;

public record OrderDetailsModelResponse(
        String uniqueId,
        String deliveryCity,
        String deliveryAddress,
        String deliveryPostAddress,
        LocalDateTime createdAt,
        String advertisementId,
        String advertisementTitle,
        String advertisementDescription,
        Category advertisementCategory,
        String advertisementCreatorEmail,
        DeliveryStatus deliveryStatus,
        OrderStatus orderStatus
) {
}
