package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.palamar.courseworkbackend.entity.order.Order;

import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, String> {

    Set<Order> getOrdersByAdvertisement_Id(String id);

}
