package ua.palamar.courseworkbackend.factory.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.OrderAdapter;
import ua.palamar.courseworkbackend.dto.request.OrderRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.order.ItemOrderEntity;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.user.UserInfo;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.factory.OrderFactory;
import ua.palamar.courseworkbackend.repository.OrderRepository;
import ua.palamar.courseworkbackend.service.ItemAdvertisementService;

@Component
public class OrderFactoryImpl implements OrderFactory {

    private final OrderAdapter orderAdapter;
    private final OrderRepository orderRepository;
    private final ItemAdvertisementService itemAdvertisementService;
    @Autowired
    public OrderFactoryImpl(OrderAdapter orderAdapter,
                            OrderRepository orderRepository,
                            ItemAdvertisementService itemAdvertisementService) {
        this.orderAdapter = orderAdapter;
        this.orderRepository = orderRepository;
        this.itemAdvertisementService = itemAdvertisementService;
    }

    @Override
    public Order createOrder(OrderRequestModel request, UserInfo buyerInfo, Advertisement advertisement) {
        Category category = advertisement.getCategory();
        return switch (category.getQualifier()) {
            case "item" -> {
                ItemAdvertisementEntity itemAdvertisement = itemAdvertisementService.getById(request.advertisementId());
                ItemOrderEntity itemOrderEntity = orderAdapter.getItemOrder(request, buyerInfo);
                buyerInfo.addOrder(itemOrderEntity);
                itemAdvertisement.addOrder(itemOrderEntity);
                orderRepository.save(itemOrderEntity);
                yield itemOrderEntity;
            }
            default -> throw new ApiRequestException(
                    String.format("Category with name %s does not exist", category)
            );
        };
    }

}
