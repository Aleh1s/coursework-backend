package ua.palamar.courseworkbackend.service;

import ua.palamar.courseworkbackend.dto.criteria.OrderCriteria;
import ua.palamar.courseworkbackend.dto.request.OrderRequest;
import ua.palamar.courseworkbackend.dto.response.*;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    OrderEntity makeOrder(OrderRequest orderRequest, HttpServletRequest request);

    void declineOrder(String id, HttpServletRequest request);

    void acceptOrder(String id, HttpServletRequest request);

    void cancelOrder(String id, HttpServletRequest request);

    void changeDeliveryStatus(String id, DeliveryStatus status, HttpServletRequest request);

    OrdersResponse getOrdersByReceiverEmail(String email, OrderCriteria criteria);

    List<OrderDetailsResponse> getOrdersByAdvertisementId(String id);
}
