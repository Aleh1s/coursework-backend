package ua.palamar.courseworkbackend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    @Query("select o from OrderEntity o join fetch o.product join fetch o.delivery where o.id = :id")
    Optional<OrderEntity> findOrderByIdJoinFetchProductAndDelivery(String id);

    @Query("select o from OrderEntity o" +
            " join fetch o.delivery " +
            "join fetch o.product " +
            "join fetch o.sender " +
            "where o.receiver.email = :email")
    List<OrderEntity> findAllByReceiverEmailJoinFetchDeliveryStatusAndSenderAndProduct(String email, Pageable pageable);

    @Query("select o from OrderEntity o " +
            "join fetch o.receiver " +
            "join fetch o.delivery " +
            "where o.product.id = :id")
    List<OrderEntity> findAllByProductIdJoinFetchDeliveryAndReceiver(String id);

    Long countAllByReceiverEmail(String email);
    Long countAllByProductId(String id);

}
