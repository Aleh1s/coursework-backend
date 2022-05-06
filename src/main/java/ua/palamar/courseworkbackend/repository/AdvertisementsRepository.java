package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.Category;

import java.util.List;

public interface AdvertisementsRepository extends JpaRepository<Advertisement, String> {

    List<Advertisement> findAllByCreatedByEmail(String email);

    List<Advertisement> findAllByCategory(Category category);

}
