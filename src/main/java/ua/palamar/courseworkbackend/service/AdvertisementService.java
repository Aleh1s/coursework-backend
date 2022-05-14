package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.Category;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AdvertisementService {

    ResponseEntity<?> save(AdvertisementRequestModel advertisementRequestModel, HttpServletRequest request);

//    ResponseEntity<?> updateAdvertisement();

    ResponseEntity<?> getAllByCategory(Category category);

    ResponseEntity<?> getSortedPageByCategory(
            Category category,
            Integer numberOfPages,
            Integer pageNumber,
            String sortBy
    );

    ResponseEntity<?> getAdvertisementResponseModelByAdvertisementId(String id);

    Advertisement getAdvertisementById(String id);

    ResponseEntity<?> getAllAdvertisementsByEmail(HttpServletRequest request);

    ResponseEntity<?> remove(String id, HttpServletRequest request);
}
