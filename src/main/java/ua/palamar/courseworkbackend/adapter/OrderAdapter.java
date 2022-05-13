package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.ItemOrderEntity;

public interface OrderAdapter {

    ItemOrderEntity getItemOrder(OrderRequestModel request, Advertisement advertisement);

}
