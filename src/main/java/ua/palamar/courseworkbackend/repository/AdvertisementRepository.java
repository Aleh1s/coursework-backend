package ua.palamar.courseworkbackend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.Category;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, String> {

    List<Advertisement> findAllByCategory(Category category);

    List<Advertisement> findAllByCategory(Category category, Pageable pageable);

    @Query("select a from Advertisement a join fetch a.creator where a.id = :id")
    Optional<Advertisement> findAdvertisementByIdJoinFetchCreator(String id);

    List<Advertisement> findAdvertisementsByCategoryAndTitleContainingIgnoreCase(Category category, String title, Pageable pageable);

    Long countAdvertisementsByCategoryAndTitleContainingIgnoreCase(Category category, String title);

    @Query("select a from Advertisement a join fetch a.image where a.id = :id")
    Optional<Advertisement> findAdvertisementByIdJoinFetchImage(String id);
    @Modifying
    @Transactional
    int removeAdvertisementById(String id);

    @Query("select a from Advertisement a join fetch a.orderEntities where a.id = :id")
    Optional<Advertisement> findAdvertisementByIdJoinFetchOrders(String id);
}
