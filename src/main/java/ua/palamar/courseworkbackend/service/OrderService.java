package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.criteria.OrderCriteria;
import ua.palamar.courseworkbackend.dto.request.OrderRequest;
import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.dto.response.OrderDetailsResponse;
import ua.palamar.courseworkbackend.dto.response.OrderResponse;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface OrderService {
    OrderEntity makeOrder(OrderRequest orderRequest, HttpServletRequest request);

    void declineOrder(String id, HttpServletRequest request);

    void acceptOrder(String id, HttpServletRequest request);

    void cancelOrder(String id, HttpServletRequest request);

    void changeDeliveryStatus(String id, DeliveryStatus status, HttpServletRequest request);

    List<OrderResponse> getOrdersByUserEmail(String email, OrderCriteria criteria);

    List<OrderDetailsResponse> getOrdersByAdvertisementId(String id, OrderCriteria criteria);
}
