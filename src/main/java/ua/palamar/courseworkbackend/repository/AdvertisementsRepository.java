package ua.palamar.courseworkbackend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;
import ua.palamar.courseworkbackend.entity.advertisement.Category;

import java.util.List;

public interface AdvertisementsRepository extends PagingAndSortingRepository<Advertisement, String> {

    List<Advertisement> findAllByCreatedByEmail(String email);

    List<Advertisement> findAllByCategory(Category category);

    List<Advertisement> findAllByCategoryAndStatus(Category category, AdvertisementStatus status, Pageable pageable);

}
