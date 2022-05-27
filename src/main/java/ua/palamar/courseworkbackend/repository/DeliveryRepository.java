package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.entity.order.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {
}
