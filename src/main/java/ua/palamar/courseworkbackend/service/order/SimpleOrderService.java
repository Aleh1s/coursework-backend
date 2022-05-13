package ua.palamar.courseworkbackend.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.dto.response.OrderDetailsModelResponse;
import ua.palamar.courseworkbackend.dto.response.OrderModelResponse;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.factory.OrderFactory;
import ua.palamar.courseworkbackend.repository.ItemAdvertisementRepository;
import ua.palamar.courseworkbackend.repository.OrderRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.OrderService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

import static ua.palamar.courseworkbackend.entity.order.DeliveryStatus.*;
import static ua.palamar.courseworkbackend.entity.order.OrderStatus.CANCELED;
import static ua.palamar.courseworkbackend.entity.order.OrderStatus.CONFIRMED;

@Service
public class SimpleOrderService implements OrderService {

    private final OrderFactory orderFactory;
    private final AdvertisementService advertisementService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final ItemAdvertisementRepository itemAdvertisementRepository;

    @Autowired
    public SimpleOrderService(OrderFactory orderFactory,
                              AdvertisementService advertisementService,
                              OrderRepository orderRepository,
                              UserService userService,
                              TokenProvider tokenProvider,
                              ItemAdvertisementRepository itemAdvertisementRepository) {
        this.orderFactory = orderFactory;
        this.advertisementService = advertisementService;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.itemAdvertisementRepository = itemAdvertisementRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<?> makeOrder(OrderRequestModel orderRequest, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        Advertisement advertisement = advertisementService.getByIdAndCategory(orderRequest.advertisementId());

        if (advertisement.getCreatedBy().getEmail().equals(email)) {
            throw new ApiRequestException(
                    String.format("User with email %s is owner", email)
            );
        }

        UserEntity buyer = userService.getUserEntityByEmail(email);
        Order order = orderFactory.createOrder(orderRequest, buyer.getUserInfo(), advertisement);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(
                        String.format("Order with id %s does not exist", id)
                ));
    }

    @Override
    @Transactional
    public ResponseEntity<?> declineOrder(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        Order order = getOrderById(id);

        Advertisement advertisement = order.getAdvertisement();

        if (!order.getDeliveryStatus().equals(IN_PROCESS)) {
            throw new ApiRequestException(
                    String.format("Can not decline order with id %s", id)
            );
        }

        if (order.getOrderStatus().equals(CANCELED)) {
            throw new ApiRequestException(
                    String.format("Can not decline order with id %s", id)
            );
        }

        if (!advertisement.getCreatedBy().getEmail().equals(email)) {
            throw new ApiRequestException(
                    String.format("User with email %s can't decline order with id %s", email, id)
            );
        }

        order.setOrderStatus(OrderStatus.DECLINED);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> acceptOrder(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        Order order = getOrderById(id);

        Advertisement advertisement = order.getAdvertisement();

        if (order.getOrderStatus().equals(CANCELED)) {
            throw new ApiRequestException(
                    String.format("Order with id %s was declined", id)
            );
        }

        if (!advertisement.getCreatedBy().getEmail().equals(email)) {
            throw new ApiRequestException(
                    String.format("User with email %s can't accept order with id %s", email, id)
            );
        }

        order.setOrderStatus(OrderStatus.CONFIRMED);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> cancelOrder(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        UserEntity user = userService.getUserEntityByEmail(email);

        Order order = getOrderById(id);

        if (!order.getOrderedBy().equals(user.getUserInfo())) {
            throw new ApiRequestException(
                    String.format("User with id %s can't cancel order with id %s", email, id)
            );
        }

        if (!order.getDeliveryStatus().equals(IN_PROCESS)) {
            throw new ApiRequestException(
                    String.format("Can not cancel order with id %s", id)
            );
        }

        order.setOrderStatus(CANCELED);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> changeDeliveryStatus(String id, String status, HttpServletRequest request) {
        Order order = getOrderById(id);
        String email = tokenProvider.getEmail(request);
        String ownerEmail = order.getAdvertisement().getCreatedBy().getEmail();

        switch (status) {
            case "IN_ROAD" -> {

                if (!email.equals(ownerEmail)) {
                    throw new ApiRequestException(
                            String.format("User with email %s can not change delivery status of order with id %s", email, id)
                    );
                }

                if (!order.getOrderStatus().equals(CONFIRMED)) {
                    throw new ApiRequestException(
                            String.format("Order is not accepted")
                    );
                }
                order.setDeliveryStatus(IN_ROAD);
            }
            case "DELIVERED" -> {

                if (!order.getOrderStatus().equals(CONFIRMED)) {
                    throw new ApiRequestException(
                            String.format("Order is not accepted")
                    );
                }
                order.setDeliveryStatus(DELIVERED);
            }
            default -> throw new ApiRequestException(
                    String.format("Invalid status %s", status)
            );
        }

        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> getOrdersByUserEmail(HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        UserEntity user = userService.getUserEntityByEmail(email);
        Set<Order> orders = orderRepository.getOrdersByOrderedBy(user.getUserInfo());
        Set<OrderModelResponse> modelResponses = orders.stream()
                .map(order -> new OrderModelResponse(
                        order.getId(),
                        order.getAdvertisement().getTitle(),
                        order.getAdvertisement().getCreatedBy().getEmail(),
                        order.getAdvertisement().getCategory(),
                        order.getOrderStatus()))
                .collect(Collectors.toSet());
        return new ResponseEntity<>(modelResponses, HttpStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public ResponseEntity<?> getOrdersByAdvertisementId(String id) {
        Set<Order> ordersByAdvertisement_id = orderRepository.getOrdersByAdvertisementId(id);
        return new ResponseEntity<>(ordersByAdvertisement_id, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getOrderDetailsModelById(String id) {
        Order order = orderRepository.getOrderById(id)
                .orElseThrow(() -> new ApiRequestException(
                        String.format(
                                "Order with id %s does not exist", id
                        )
                ));

        Advertisement advertisement = order.getAdvertisement();
        OrderDetailsModelResponse orderDetailsModelResponse = new OrderDetailsModelResponse(
                order.getId(),
                order.getCity(),
                order.getAddress(),
                order.getPostNumber(),
                order.getCreatedAt(),
                advertisement.getId(),
                advertisement.getTitle(),
                advertisement.getDescription(),
                advertisement.getCategory(),
                advertisement.getCreatedBy().getEmail(),
                order.getDeliveryStatus(),
                order.getOrderStatus()
                );

        return new ResponseEntity<>(orderDetailsModelResponse, HttpStatus.ACCEPTED);
    }


    @Override
    public ResponseEntity<?> deleteOrder(String id, HttpServletRequest request) {
        return null;
    }
}
