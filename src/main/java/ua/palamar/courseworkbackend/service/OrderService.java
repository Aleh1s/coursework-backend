package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    ResponseEntity<?> makeOrder(OrderRequestModel orderRequestModel, HttpServletRequest request);

    ResponseEntity<?> declineOrder(String id, HttpServletRequest request);

    ResponseEntity<?> acceptOrder(String id, HttpServletRequest request);

    ResponseEntity<?> cancelOrder(String id, HttpServletRequest request);

    ResponseEntity<?> changeDeliveryStatus(String id, DeliveryStatus status, HttpServletRequest request);

    ResponseEntity<?> getSortedPageOfOrdersByUserEmail(HttpServletRequest request);

    ResponseEntity<?> getOrdersByAdvertisementId(String id);

    ResponseEntity<?> deleteOrder(String id, HttpServletRequest request);
}
