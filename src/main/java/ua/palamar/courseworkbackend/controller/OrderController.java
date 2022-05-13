package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.service.OrderService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> makeOrder(
            @RequestBody OrderRequestModel model,
            HttpServletRequest request
    ) {
        return orderService.makeOrder(model, request);
    }

    @PutMapping("/decline")
    public ResponseEntity<?> declineOrder(
            @RequestParam("_id") String id,
            HttpServletRequest request
    ) {
        return orderService.declineOrder(id, request);
    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptOrder(
            @RequestParam("_id") String id,
            HttpServletRequest request
    ) {
        return orderService.acceptOrder(id, request);
    }

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelOrder(
            @RequestParam("_id") String id,
            HttpServletRequest request
    ) {
        return orderService.cancelOrder(id, request);
    }

    @PutMapping("/change-status")
    public ResponseEntity<?> changeDeliveryStatus(
            @RequestParam("_id") String id,
            @RequestParam("_status") String status,
            HttpServletRequest request
    ) {
        return orderService.changeDeliveryStatus(id, status, request);
    }

    @GetMapping("/email")
    public ResponseEntity<?> getAllByEmail(
            HttpServletRequest request
    ) {
        return orderService.getOrdersByUserEmail(request);
    }

    @GetMapping("/advertisement")
    public ResponseEntity<?> getAllByAdvertisementId(
            @RequestParam("_id") String id
    ) {
        return orderService.getOrdersByAdvertisementId(id);
    }
}
