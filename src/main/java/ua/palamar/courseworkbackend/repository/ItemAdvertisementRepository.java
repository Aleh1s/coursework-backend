package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;

import java.util.List;
import java.util.Optional;

public interface ItemAdvertisementRepository extends JpaRepository<ItemAdvertisementEntity, String> {

    @Query("select i from ItemAdvertisementEntity i join fetch i.dimensions join fetch i.createdBy where i.id = :id")
    Optional<ItemAdvertisementEntity> getItemAdvertisementEntityById(String id);

}
