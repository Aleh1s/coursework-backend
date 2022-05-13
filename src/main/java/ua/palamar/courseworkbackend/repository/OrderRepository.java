package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.user.UserInfo;

import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("select o from Order o join fetch o.advertisement where o.advertisement.id = :id")
    Set<Order> getOrdersByAdvertisementId(String id);

    @Query("select o from Order o join fetch o.advertisement where o.orderedBy = :orderedBy")
    Set<Order> getOrdersByOrderedBy(UserInfo orderedBy);

    @Query("select o from Order o join fetch o.advertisement join fetch o.orderedBy where o.id = :id")
    Optional<Order> getOrderById(String id);

}
