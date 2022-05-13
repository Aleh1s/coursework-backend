package ua.palamar.courseworkbackend.adapter.order;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.OrderAdapter;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.entity.order.ItemOrderEntity;
import ua.palamar.courseworkbackend.entity.user.UserInfo;

@Component
public class OrderAdapterImpl implements OrderAdapter {

    @Override
    public ItemOrderEntity getItemOrder(OrderRequestModel request, UserInfo userInfo) {
        return new ItemOrderEntity(
                request.city().orElse(userInfo.getCity()),
                request.address().orElse(userInfo.getAddress()),
                request.postNumber().orElse(userInfo.getPostNumber()),
                request.wishes().orElse(null)
        );
    }
}
