package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.palamar.courseworkbackend.entity.order.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {
}
