package ua.palamar.courseworkbackend.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.adapter.user.UserDtoAdapter;
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
import ua.palamar.courseworkbackend.service.OrderService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    private final UserDtoAdapter userDtoAdapter;

    @Autowired
    public OrderServiceImpl(
            DeliveryRepository deliveryRepository,
            AdvertisementRepository advertisementRepository,
            OrderRepository orderRepository,
            UserService userService,
            TokenProvider tokenProvider,
            UserDtoAdapter userDtoAdapter) {
        this.deliveryRepository = deliveryRepository;
        this.advertisementRepository = advertisementRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.userDtoAdapter = userDtoAdapter;
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

        String city = orderRequest.city().trim();
        String address = orderRequest.address().trim();
        String postOffice = orderRequest.postOffice().trim();
        String wishes = orderRequest.wishes().trim();

        Delivery delivery = new Delivery(
                city,
                address,
                postOffice
        );

        OrderEntity orderEntity = new OrderEntity(
                delivery,
                owner,
                wishes
        );

        orderEntity.addAdvertisement(advertisement);
        orderEntity.addReceiver(receiver);

        deliveryRepository.save(delivery);
        orderRepository.save(orderEntity);

        return orderEntity;
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
                    String.format("Order with id %s was canceled", id)
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
        UserAccount receiver = orderEntity.getReceiver();

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

                if (!email.equals(receiver.getEmail())) {
                    throw new ApiRequestException(
                            String.format("User with email %s can not change delivery status of order with id %s", email, id)
                    );
                }

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
    public OrdersResponse getOrdersByReceiverEmail(String email, OrderCriteria criteria) {
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
                    UserResponse senderModel = userDtoAdapter.getModel(sender);

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
        return getOrdersDetails(orders);
    }

    private List<OrderDetailsResponse> getOrdersDetails(List<OrderEntity> orders) {
        return orders.stream()
                .map(orderEntity -> {
                    UserAccount receiver = orderEntity.getReceiver();
                    return new OrderDetailsResponse(
                            orderEntity.getId(),
                            orderEntity.getCreatedAt(),
                            orderEntity.getOrderStatus(),
                            orderEntity.getDelivery(),
                            userDtoAdapter.getModel(receiver),
                            orderEntity.getWishes()
                    );
                }).collect(Collectors.toList());
    }
}
