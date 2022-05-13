package ua.palamar.courseworkbackend.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.factory.OrderFactory;
import ua.palamar.courseworkbackend.repository.OrderRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.OrderService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Service
public class SimpleOrderService implements OrderService {

    private final OrderFactory orderFactory;
    private final AdvertisementService advertisementService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    @Autowired
    public SimpleOrderService(OrderFactory orderFactory,
                              AdvertisementService advertisementService,
                              OrderRepository orderRepository,
                              UserService userService,
                              TokenProvider tokenProvider) {
        this.orderFactory = orderFactory;
        this.advertisementService = advertisementService;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public ResponseEntity<?> makeOrder(OrderRequestModel orderRequest, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        UserEntity buyer = userService.getUserEntityByEmail(email);
        Advertisement advertisement = advertisementService.getById(orderRequest.advertisementId());
        Order order = orderFactory.createOrder(orderRequest, advertisement, buyer.getUserInfo());
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

        if (!order.getStatus().equals(DeliveryStatus.IN_PROCESS)) {
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

        if (order.getOrderStatus().equals(OrderStatus.DECLINED)) {
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
    public ResponseEntity<?> inRoad(String id) {
        Order order = getOrderById(id);

        order.setStatus(DeliveryStatus.IN_ROAD);

        return ResponseEntity.ok().build();
    }


    @Override
    @Transactional
    public ResponseEntity<?> onDelivered(String id) {
        Order order = getOrderById(id);

        order.setStatus(DeliveryStatus.DELIVERED);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteOrder(String id, HttpServletRequest request) {
        return null;
    }
}
