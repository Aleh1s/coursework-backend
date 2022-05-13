package ua.palamar.courseworkbackend.adapter.order;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.OrderAdapter;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.ItemOrderEntity;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;

@Component
public class OrderAdapterImpl implements OrderAdapter {

    @Override
    public ItemOrderEntity getItemOrder(OrderRequestModel request, Advertisement advertisement) {
        return new ItemOrderEntity(
                null,
                null,
                advertisement,
                DeliveryStatus.IN_PROCESS,
                null,
                OrderStatus.UNCONFIRMED
        );
    }
}
