package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;

import java.util.List;

public interface AdvertisementsRepository extends JpaRepository<Advertisement, String> {

    List<Advertisement> findAllByCreatedByEmail(String email);

}
