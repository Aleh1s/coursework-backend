package ua.palamar.courseworkbackend.factory.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.OrderAdapter;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.entity.user.UserInfo;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.factory.OrderFactory;
import ua.palamar.courseworkbackend.repository.OrderRepository;

@Component
public class OrderFactoryImpl implements OrderFactory {

    private final OrderAdapter orderAdapter;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderFactoryImpl(OrderAdapter orderAdapter,
                            OrderRepository orderRepository) {
        this.orderAdapter = orderAdapter;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(OrderRequestModel request, Advertisement advertisement, UserInfo buyerInfo) {
        Order order;

        String category = request.advertisementCategory();
        return switch (category) {
            case "ITEM" -> {
                order = orderAdapter.getItemOrder(request, advertisement);
                buyerInfo.addOrder(order);
                orderRepository.save(order);
                yield order;
            }
            default -> throw new ApiRequestException(
                    String.format("Category with name %s does not exist", category)
            );
        };
    }

}
