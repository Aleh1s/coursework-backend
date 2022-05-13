package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    ResponseEntity<?> makeOrder(OrderRequestModel orderRequestModel, HttpServletRequest request);

    ResponseEntity<?> declineOrder(String id, HttpServletRequest request);

    ResponseEntity<?> acceptOrder(String id, HttpServletRequest request);

    ResponseEntity<?> cancelOrder(String id, HttpServletRequest request);

    ResponseEntity<?> changeDeliveryStatus(String id, String status, HttpServletRequest request);

    ResponseEntity<?> deleteOrder(String id, HttpServletRequest request);
}
