package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.entity.DeliveryInfoEntity;

@Repository
public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfoEntity, String> {

}
