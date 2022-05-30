package ua.palamar.courseworkbackend.repository;

import com.sun.istack.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementCategory;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, String> {

    @Query("select a from Advertisement a join fetch a.creator where a.id = :id")
    Optional<Advertisement> findAdvertisementByIdJoinFetchCreator(String id);

    List<Advertisement> findAdvertisementsByCategoryAndStatusAndTitleContainingIgnoreCase(AdvertisementCategory category, AdvertisementStatus status, String title, Pageable pageable);

    Long countAdvertisementsByCategoryAndStatusAndTitleContainingIgnoreCase(AdvertisementCategory category, AdvertisementStatus status, String title);

    @Query("select a from Advertisement a join fetch a.image where a.id = :id")
    Optional<Advertisement> findAdvertisementByIdJoinFetchImage(String id);
    @Modifying
    @Transactional
    void removeAdvertisementById(String id);

    List<Advertisement> findAdvertisementsByCreatorEmail(String email, Pageable pageable);
    Long countAdvertisementsByCreatorEmail(String email);

    boolean existsById(String id);

    @Transactional
    @Modifying
    @Query("update Advertisement a set a.status = :status where a.id = :id")
    void updateAdvertisementStatusById(String id, AdvertisementStatus status);

    List<Advertisement> findAdvertisementsByStatus(AdvertisementStatus status, Pageable pageable);

    Long countAllByStatus(AdvertisementStatus status);
}
