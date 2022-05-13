package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;

public record OrderModelResponse(
        String uniqueId,
        String title,
        String sender,
        Category category,
        OrderStatus orderStatus
) {
}
