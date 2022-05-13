package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementStatus;
import ua.palamar.courseworkbackend.entity.advertisement.Category;

public interface GeneralizedAdvertisementService {

    ResponseEntity<?> getAllAdvertisementsByEmail(String email);
    ResponseEntity<?> getAllAdvertisementsByCategory(Category category);
    ResponseEntity<?> getPageOfSortedAdvertisementsByCategoryAndStatus(
            Category category,
            ItemAdvertisementStatus status,
            Integer numberOfPages,
            Integer pageNumber
    );
    ResponseEntity<?> getAdvertisementById(String category, String id);
}
