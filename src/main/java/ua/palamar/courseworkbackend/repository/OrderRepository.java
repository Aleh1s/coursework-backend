package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;

import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    @Query("select o from OrderEntity o join fetch o.product join fetch o.deliveryEntity where o.id = :id")
    Optional<OrderEntity> findOrderByIdJoinFetchProductAndDelivery(String id);

    @Query("select o from OrderEntity o" +
            " join fetch o.deliveryEntity " +
            "join fetch o.product " +
            "join fetch o.sender " +
            "where o.receiver.email = :email")
    Set<OrderEntity> findAllByReceiverEmailJoinFetchDeliveryStatusAndSenderAndProduct(String email);

    @Query("select o from OrderEntity o " +
            "join fetch o.receiver " +
            "join fetch o.deliveryEntity " +
            "where o.product.id = :id")
    Set<OrderEntity> findAllByProductIdJoinFetchDeliveryAndReceiver(String id);



}
