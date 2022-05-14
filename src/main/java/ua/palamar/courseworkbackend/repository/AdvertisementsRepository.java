package ua.palamar.courseworkbackend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.Category;

import java.util.List;
import java.util.Optional;

public interface AdvertisementsRepository extends PagingAndSortingRepository<Advertisement, String> {

    List<Advertisement> findAllByCreatorEmail(String email);

    List<Advertisement> findAllByCategory(Category category);

    List<Advertisement> findAllByCategory(Category category, Pageable pageable);

    @Query("select a from Advertisement a join fetch a.creator where a.id = :id")
    Optional<Advertisement> findAdvertisementByIdJoinFetchCreator(String id);

    @Query("select a from Advertisement a join fetch a.orderEntities where a.id = :id")
    Optional<Advertisement> findAdvertisementByIdJoinFetchOrders(String id);

}
