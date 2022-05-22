package ua.palamar.courseworkbackend.service;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.image.ImageEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public interface AdvertisementService {

    ResponseEntity<?> save(AdvertisementRequestModel advertisementRequestModel, HttpServletRequest request, MultipartFile file);

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

    ResponseEntity<?> findAdvertisementsByCategoryAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(Category category, String query, String sortBy, Integer limit, Integer page);

    ImageEntity getImageById(String id);
}
