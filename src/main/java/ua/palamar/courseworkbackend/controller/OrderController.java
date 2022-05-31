package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.criteria.OrderCriteria;
import ua.palamar.courseworkbackend.dto.request.OrderRequest;
import ua.palamar.courseworkbackend.dto.response.OrderDetailsResponse;
import ua.palamar.courseworkbackend.dto.response.OrdersResponse;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;
import ua.palamar.courseworkbackend.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin("http://localhost:3000")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderEntity> makeOrder(
            @RequestBody OrderRequest model,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(orderService.makeOrder(model, request), HttpStatus.OK);
    }

    @PatchMapping("/decline")
    public ResponseEntity<?> declineOrder(
            @RequestParam("_id") String id,
            HttpServletRequest request
    ) {
        orderService.declineOrder(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/accept")
    public ResponseEntity<?> acceptOrder(
            @RequestParam("_id") String id,
            HttpServletRequest request
    ) {
        orderService.acceptOrder(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelOrder(
            @RequestParam("_id") String id,
            HttpServletRequest request
    ) {
        orderService.cancelOrder(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-status")
    public ResponseEntity<?> changeDeliveryStatus(
            @RequestParam("_id") String id,
            @RequestParam("_status") DeliveryStatus status,
            HttpServletRequest request
    ) {
        orderService.changeDeliveryStatus(id, status, request);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/users/{email}")
    public ResponseEntity<OrdersResponse> getAllByUserEmail(
            @PathVariable("email") String email,
            @RequestParam(value = "_limit", defaultValue = "12") Integer limit,
            @RequestParam(value = "_page", defaultValue = "0") Integer page,
            @RequestParam(value = "_sortBy", defaultValue = "createdAt") String sortBy
    ) {
        return new ResponseEntity<>(orderService.getOrdersByReceiverEmail(email, new OrderCriteria(limit, page, sortBy)), HttpStatus.OK);
    }
    @GetMapping("/advertisements/{id}")
    public ResponseEntity<List<OrderDetailsResponse>> getAllByAdvertisementId(
            @PathVariable("id") String id
    ) {
        return new ResponseEntity<>(orderService.getOrdersByAdvertisementId(id), HttpStatus.OK);
    }
}
