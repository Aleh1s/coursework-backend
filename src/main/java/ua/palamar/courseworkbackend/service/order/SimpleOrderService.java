package ua.palamar.courseworkbackend.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.dto.response.OrderDetailsModelResponse;
import ua.palamar.courseworkbackend.dto.response.OrderResponseModel;
import ua.palamar.courseworkbackend.dto.response.UserResponseModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.DeliveryEntity;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
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
    public ResponseEntity<?> makeOrder(OrderRequestModel orderRequest, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        String advertisementId = orderRequest.advertisementId();

        Advertisement advertisement = advertisementRepository.findAdvertisementByIdJoinFetchCreator(advertisementId)
                .orElseThrow(() -> new ApiRequestException(
                        String.format(
                                "Advertisement with id %s does not exist", advertisementId
                        )
                ));

        UserEntity owner = advertisement.getCreator();

        if (owner.getEmail().equals(email)) {
            throw new ApiRequestException(
                    String.format("User with email %s is owner", email)
            );
        }

        UserEntity receiver = userService.getUserEntityByEmail(email);

        boolean receiverOrderedBefore = receiverOrderedBefore(receiver, advertisement);

        if (receiverOrderedBefore) {
            throw new ApiRequestException(
                    String.format(
                        "User %s already has order for this advertisement", receiver.getEmail()
                    )
            );
        }

        DeliveryEntity deliveryEntity = new DeliveryEntity(
                orderRequest.city(),
                orderRequest.address(),
                orderRequest.postOffice()
        );

        OrderEntity orderEntity = new OrderEntity(
                deliveryEntity,
                owner,
                orderRequest.wishes()
        );

        orderEntity.addAdvertisement(advertisement);
        orderEntity.addReceiver(receiver);

        deliveryRepository.save(deliveryEntity);
        orderRepository.save(orderEntity);

        return new ResponseEntity<>(orderEntity, HttpStatus.CREATED);
    }

    private boolean receiverOrderedBefore(UserEntity receiver, Advertisement advertisement) {
        return receiver.getOrderEntities().stream()
                .anyMatch(order -> order.getProduct() == advertisement);
    }

    public OrderEntity getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(
                        String.format("Order with id %s does not exist", id)
                ));
    }

    @Override
    @Transactional
    public ResponseEntity<?> declineOrder(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        OrderEntity orderEntity = orderRepository.findOrderByIdJoinFetchProductAndDelivery(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format("Order with product id %s does not exist", id)
                        )
                );

        Advertisement advertisement = orderEntity.getProduct();
        DeliveryEntity deliveryEntity = orderEntity.getDeliveryEntity();

        if (!deliveryEntity.getDeliveryStatus().equals(IN_PROCESS)) {
            throw new ApiRequestException(
                    String.format("Can not decline order with id %s", id)
            );
        }

        if (orderEntity.getOrderStatus().equals(CANCELED)) {
            throw new ApiRequestException(
                    String.format("Can not decline order with id %s", id)
            );
        }

        UserEntity creator = advertisement.getCreator();
        if (!creator.getEmail().equals(email)) {
            throw new ApiRequestException(
                    String.format("User with email %s can't decline order with id %s", email, id)
            );
        }

        orderEntity.setOrderStatus(OrderStatus.DECLINED);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> acceptOrder(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        OrderEntity orderEntity = orderRepository.findOrderByIdJoinFetchProductAndDelivery(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format("Order with product id %s does not exist", id)
                        )
                );

        Advertisement advertisement = orderEntity.getProduct();

        if (orderEntity.getOrderStatus().equals(CANCELED)) {
            throw new ApiRequestException(
                    String.format("Order with id %s was declined", id)
            );
        }

        UserEntity creator = advertisement.getCreator();
        if (!creator.getEmail().equals(email)) {
            throw new ApiRequestException(
                    String.format("User with email %s can't accept order with id %s", email, id)
            );
        }

        orderEntity.setOrderStatus(OrderStatus.CONFIRMED);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> cancelOrder(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        UserEntity user = userService.getUserEntityByEmail(email);

        OrderEntity orderEntity = getOrderById(id);

        if (!orderEntity.getReceiver().equals(user)) {
            throw new ApiRequestException(
                    String.format("User with email %s can't cancel order with id %s", email, id)
            );
        }

        DeliveryEntity deliveryEntity = orderEntity.getDeliveryEntity();
        if (!deliveryEntity.getDeliveryStatus().equals(IN_PROCESS)) {
            throw new ApiRequestException(
                    String.format("Can not cancel order with id %s", id)
            );
        }

        orderEntity.setOrderStatus(CANCELED);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> changeDeliveryStatus(String id, DeliveryStatus status, HttpServletRequest request) {
        OrderEntity orderEntity = getOrderById(id);
        String email = tokenProvider.getEmail(request);

        UserEntity sender = orderEntity.getSender();

        final DeliveryEntity deliveryEntity = orderEntity.getDeliveryEntity();

        final String orderIsNotConfirmed = "Order is not CONFIRMED";
        switch (status.getQualifier()) {
            case "in_road" -> {

                if (!email.equals(sender.getEmail())) {
                    throw new ApiRequestException(
                            String.format("User with email %s can not change delivery status of order with id %s", email, id)
                    );
                }

                if (!orderEntity.getOrderStatus().equals(CONFIRMED)) {
                    throw new ApiRequestException(
                            orderIsNotConfirmed
                    );
                }
                deliveryEntity.setDeliveryStatus(IN_ROAD);
            }
            case "delivered" -> {

                if (!orderEntity.getOrderStatus().equals(CONFIRMED)) {
                    throw new ApiRequestException(
                            orderIsNotConfirmed
                    );
                }
                deliveryEntity.setDeliveryStatus(DELIVERED);
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

        Set<OrderEntity> orderEntities = orderRepository.findAllByReceiverEmailJoinFetchDeliveryStatusAndSenderAndProduct(email);

        Set<OrderResponseModel> responseModels = orderEntities.stream()
                .map(orderEntity -> {
                    UserEntity sender = orderEntity.getSender();
                    UserResponseModel senderModel = new UserResponseModel(
                            sender.getEmail(),
                            sender.getFirstName(),
                            sender.getLastName(),
                            sender.getPhoneNumber()
                    );

                    return new OrderResponseModel(
                            orderEntity.getId(),
                            orderEntity.getCreatedAt(),
                            orderEntity.getOrderStatus(),
                            orderEntity.getDeliveryEntity(),
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
        Set<OrderEntity> orders = orderRepository.findAllByProductIdJoinFetchDeliveryAndReceiver(id);

        Set<OrderDetailsModelResponse> responses = orders.stream()
                .map(orderEntity -> new OrderDetailsModelResponse(
                        orderEntity.getId(),
                        orderEntity.getCreatedAt(),
                        orderEntity.getOrderStatus(),
                        orderEntity.getDeliveryEntity(),
                        new UserResponseModel(
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
