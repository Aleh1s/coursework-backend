package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.entity.order.ItemOrderEntity;
import ua.palamar.courseworkbackend.entity.user.UserInfo;

public interface OrderAdapter {

    ItemOrderEntity getItemOrder(OrderRequestModel request, UserInfo userInfo);

}
