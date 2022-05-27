package ua.palamar.courseworkbackend.service.order;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.dto.criteria.OrderCriteria;
import ua.palamar.courseworkbackend.dto.request.OrderRequest;
import ua.palamar.courseworkbackend.dto.response.*;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.Delivery;
import ua.palamar.courseworkbackend.entity.order.DeliveryStatus;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ua.palamar.courseworkbackend.entity.order.DeliveryStatus.*;
import static ua.palamar.courseworkbackend.entity.order.OrderStatus.CANCELED;
import static ua.palamar.courseworkbackend.entity.order.OrderStatus.CONFIRMED;

@Service
public class OrderServiceImpl implements OrderService {

    private final DeliveryRepository deliveryRepository;
    private final AdvertisementRepository advertisementRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Autowired
    public OrderServiceImpl(
            DeliveryRepository deliveryRepository,
            AdvertisementRepository advertisementRepository,
            OrderRepository orderRepository,
            UserService userService,
            TokenProvider tokenProvider
    ) {
        this.deliveryRepository = deliveryRepository;
        this.advertisementRepository = advertisementRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public OrderEntity makeOrder(OrderRequest orderRequest, HttpServletRequest request) {
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

        OrderEntity orderEntity = new OrderEntity(
                delivery,
                owner,
                orderRequest.wishes()
        );

        orderEntity.addAdvertisement(advertisement);
        orderEntity.addReceiver(receiver);

        deliveryRepository.save(delivery);
        orderRepository.save(orderEntity);

        return orderEntity;
    }

    private boolean receiverOrderedBefore(UserAccount receiver, Advertisement advertisement) {
        return receiver.getOrderEntityEntities().stream()
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
    public void declineOrder(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        OrderEntity orderEntity = orderRepository.findOrderByIdJoinFetchProductAndDelivery(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format("Order with product id %s does not exist", id)
                        )
                );

        Advertisement advertisement = orderEntity.getProduct();
        Delivery delivery = orderEntity.getDelivery();

        if (!delivery.getDeliveryStatus().equals(IN_PROCESS)) {
            throw new ApiRequestException(
                    String.format("Can not decline order with id %s", id)
            );
        }

        if (orderEntity.getOrderStatus().equals(CANCELED)) {
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

        orderEntity.setOrderStatus(OrderStatus.DECLINED);
    }

    @Override
    @Transactional
    public void acceptOrder(String id, HttpServletRequest request) {
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

        UserAccount creator = advertisement.getCreator();
        if (!creator.getEmail().equals(email)) {
            throw new ApiRequestException(
                    String.format("User with email %s can't accept order with id %s", email, id)
            );
        }

        orderEntity.setOrderStatus(OrderStatus.CONFIRMED);
    }

    @Override
    @Transactional
    public void cancelOrder(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        UserAccount user = userService.getUserEntityByEmail(email);

        OrderEntity orderEntity = getOrderById(id);

        if (!orderEntity.getReceiver().equals(user)) {
            throw new ApiRequestException(
                    String.format("User with email %s can't cancel order with id %s", email, id)
            );
        }

        Delivery delivery = orderEntity.getDelivery();
        if (!delivery.getDeliveryStatus().equals(IN_PROCESS)) {
            throw new ApiRequestException(
                    String.format("Can not cancel order with id %s", id)
            );
        }

        orderEntity.setOrderStatus(CANCELED);
    }

    @Override
    @Transactional
    public void changeDeliveryStatus(String id, DeliveryStatus status, HttpServletRequest request) {
        OrderEntity orderEntity = getOrderById(id);
        String email = tokenProvider.getEmail(request);

        UserAccount sender = orderEntity.getSender();

        final Delivery delivery = orderEntity.getDelivery();

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
                delivery.setDeliveryStatus(IN_ROAD);
            }
            case "delivered" -> {

                if (!orderEntity.getOrderStatus().equals(CONFIRMED)) {
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
    }

    @Override
    @Transactional
    public OrdersResponse getOrdersByUserEmail(String email, OrderCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.page(), criteria.limit(), Sort.by(criteria.sortBy()));

        List<OrderEntity> orders =
                orderRepository.findAllByReceiverEmailJoinFetchDeliveryStatusAndSenderAndProduct(email, pageable);
        Long count = orderRepository.countAllByReceiverEmail(email);

        return new OrdersResponse(getOrderResponses(orders), count);
    }

    public List<OrderResponse> getOrderResponses(List<OrderEntity> orders) {
        return orders.stream()
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
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailsResponse> getOrdersByAdvertisementId(String id) {
        List<OrderEntity> orders = orderRepository.findAllByProductIdJoinFetchDeliveryAndReceiver(id);
        return getOrderDetails(orders);
    }

    private List<OrderDetailsResponse> getOrderDetails(List<OrderEntity> orders) {
        return orders.stream()
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
                )).collect(Collectors.toList());
    }
}
