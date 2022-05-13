package ua.palamar.courseworkbackend.factory;

import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.entity.user.UserInfo;

public interface OrderFactory {

    Order createOrder(OrderRequestModel request, Advertisement advertisement, UserInfo buyerInfo);

}
