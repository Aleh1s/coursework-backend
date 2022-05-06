package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;

public interface ItemAdvertisementRepository extends JpaRepository<ItemAdvertisementEntity, String> {



}
