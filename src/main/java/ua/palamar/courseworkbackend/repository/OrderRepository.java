package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.palamar.courseworkbackend.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
}
