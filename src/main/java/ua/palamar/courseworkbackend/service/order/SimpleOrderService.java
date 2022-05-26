package ua.palamar.courseworkbackend.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.dto.request.OrderRequest;
import ua.palamar.courseworkbackend.dto.response.OrderDetailsResponse;
import ua.palamar.courseworkbackend.dto.response.OrderResponse;
import ua.palamar.courseworkbackend.dto.response.UserResponse;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.Delivery;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.AdvertisementRepository;
import ua.palamar.courseworkbackend.repository.DeliveryRepository;
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

    private final AdvertisementService advertisementService;
    private final DeliveryRepository deliveryRepository;
    private final AdvertisementRepository advertisementRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Autowired
    public SimpleOrderService(
            AdvertisementService advertisementService,
            DeliveryRepository deliveryRepository,
            AdvertisementRepository advertisementRepository,
            OrderRepository orderRepository,
            UserService userService,
            TokenProvider tokenProvider
    ) {
        this.advertisementService = advertisementService;
        this.deliveryRepository = deliveryRepository;
        this.advertisementRepository = advertisementRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public ResponseEntity<?> makeOrder(OrderRequest orderRequest, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        String advertisementId = orderRequest.advertisementId();

        Advertisement advertisement = advertisementRepository.findAdvertisementByIdJoinFetchCreator(advertisementId)
                .orElseThrow(() -> new ApiRequestException(
                        String.format(
                                "Advertisement with id %s does not exist", advertisementId
                        )
                ));

        UserAccount owner = advertisement.getCreator();

        if (owner.getEmail().equals(email)) {
            throw new ApiRequestException(
                    String.format("User with email %s is owner", email)
            );
        }

        UserAccount receiver = userService.getUserEntityByEmail(email);

        boolean receiverOrderedBefore = receiverOrderedBefore(receiver, advertisement);

        if (receiverOrderedBefore) {
            throw new ApiRequestException(
                    String.format(
                        "User %s already has order for this advertisement", receiver.getEmail()
                    )
            );
        }

        Delivery delivery = new Delivery(
                orderRequest.city(),
                orderRequest.address(),
                orderRequest.postOffice()
        );

        Order order = new Order(
                delivery,
                owner,
                orderRequest.wishes()
        );

        order.addAdvertisement(advertisement);
        order.addReceiver(receiver);

        deliveryRepository.save(delivery);
        orderRepository.save(order);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    private boolean receiverOrderedBefore(UserAccount receiver, Advertisement advertisement) {
        return receiver.getOrderEntities().stream()
                .anyMatch(order -> order.getProduct() == advertisement);
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

        Order order = orderRepository.findOrderByIdJoinFetchProductAndDelivery(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format("Order with product id %s does not exist", id)
                        )
                );

        Advertisement advertisement = order.getProduct();
        Delivery delivery = order.getDelivery();

        if (!delivery.getDeliveryStatus().equals(IN_PROCESS)) {
            throw new ApiRequestException(
                    String.format("Can not decline order with id %s", id)
            );
        }

        if (order.getOrderStatus().equals(CANCELED)) {
            throw new ApiRequestException(
                    String.format("Can not decline order with id %s", id)
            );
        }

        UserAccount creator = advertisement.getCreator();
        if (!creator.getEmail().equals(email)) {
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

        Order order = orderRepository.findOrderByIdJoinFetchProductAndDelivery(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format("Order with product id %s does not exist", id)
                        )
                );

        Advertisement advertisement = order.getProduct();

        if (order.getOrderStatus().equals(CANCELED)) {
            throw new ApiRequestException(
                    String.format("Order with id %s was declined", id)
            );
        }

        UserAccount creator = advertisement.getCreator();
        if (!creator.getEmail().equals(email)) {
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
        UserAccount user = userService.getUserEntityByEmail(email);

        Order order = getOrderById(id);

        if (!order.getReceiver().equals(user)) {
            throw new ApiRequestException(
                    String.format("User with email %s can't cancel order with id %s", email, id)
            );
        }

        Delivery delivery = order.getDelivery();
        if (!delivery.getDeliveryStatus().equals(IN_PROCESS)) {
            throw new ApiRequestException(
                    String.format("Can not cancel order with id %s", id)
            );
        }

        order.setOrderStatus(CANCELED);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> changeDeliveryStatus(String id, DeliveryStatus status, HttpServletRequest request) {
        Order order = getOrderById(id);
        String email = tokenProvider.getEmail(request);

        UserAccount sender = order.getSender();

        final Delivery delivery = order.getDelivery();

        final String orderIsNotConfirmed = "Order is not CONFIRMED";
        switch (status.getQualifier()) {
            case "in_road" -> {

                if (!email.equals(sender.getEmail())) {
                    throw new ApiRequestException(
                            String.format("User with email %s can not change delivery status of order with id %s", email, id)
                    );
                }

                if (!order.getOrderStatus().equals(CONFIRMED)) {
                    throw new ApiRequestException(
                            orderIsNotConfirmed
                    );
                }
                delivery.setDeliveryStatus(IN_ROAD);
            }
            case "delivered" -> {

                if (!order.getOrderStatus().equals(CONFIRMED)) {
                    throw new ApiRequestException(
                            orderIsNotConfirmed
                    );
                }
                delivery.setDeliveryStatus(DELIVERED);
            }
            default -> throw new ApiRequestException(
                    String.format("Invalid status %s", status)
            );
        }

        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> getSortedPageOfOrdersByUserEmail(HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        Set<Order> orderEntities = orderRepository.findAllByReceiverEmailJoinFetchDeliveryStatusAndSenderAndProduct(email);

        Set<OrderResponse> responseModels = orderEntities.stream()
                .map(orderEntity -> {
                    UserAccount sender = orderEntity.getSender();
                    UserResponse senderModel = new UserResponse(
                            sender.getEmail(),
                            sender.getFirstName(),
                            sender.getLastName(),
                            sender.getPhoneNumber()
                    );

                    return new OrderResponse(
                            orderEntity.getId(),
                            orderEntity.getCreatedAt(),
                            orderEntity.getOrderStatus(),
                            orderEntity.getDelivery(),
                            orderEntity.getProduct(),
                            senderModel,
                            orderEntity.getWishes()
                    );
                })
                .collect(Collectors.toSet());

        return new ResponseEntity<>(responseModels, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getOrdersByAdvertisementId(String id) {
        Set<Order> orders = orderRepository.findAllByProductIdJoinFetchDeliveryAndReceiver(id);

        Set<OrderDetailsResponse> responses = orders.stream()
                .map(orderEntity -> new OrderDetailsResponse(
                        orderEntity.getId(),
                        orderEntity.getCreatedAt(),
                        orderEntity.getOrderStatus(),
                        orderEntity.getDelivery(),
                        new UserResponse(
                                orderEntity.getReceiver().getEmail(),
                                orderEntity.getReceiver().getFirstName(),
                                orderEntity.getReceiver().getLastName(),
                                orderEntity.getReceiver().getPhoneNumber()
                        ),
                        orderEntity.getWishes()
                )).collect(Collectors.toSet());

        return new ResponseEntity<>(responses, HttpStatus.ACCEPTED);
    }
    @Override
    public ResponseEntity<?> deleteOrder(String id, HttpServletRequest request) {
        return null;
    }
}
