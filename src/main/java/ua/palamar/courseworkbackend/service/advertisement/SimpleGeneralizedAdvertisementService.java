package ua.palamar.courseworkbackend.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.dto.response.AdvertisementPageResponseModel;
import ua.palamar.courseworkbackend.dto.response.ItemAdvertisementResponse;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.user.UserInfo;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.AdvertisementsRepository;
import ua.palamar.courseworkbackend.repository.ItemAdvertisementRepository;

import java.util.List;

@Service
public class SimpleGeneralizedAdvertisementService implements ua.palamar.courseworkbackend.service.GeneralizedAdvertisementService {

    private final AdvertisementsRepository advertisementsRepository;
    private final ItemAdvertisementRepository itemAdvertisementRepository;

    @Autowired
    public SimpleGeneralizedAdvertisementService(AdvertisementsRepository advertisementsRepository,
                                                 ItemAdvertisementRepository itemAdvertisementRepository) {
        this.advertisementsRepository = advertisementsRepository;
        this.itemAdvertisementRepository = itemAdvertisementRepository;
    }

    public ResponseEntity<?> getAllAdvertisementsByCategory(Category category) {
        List<Advertisement> advertisements = advertisementsRepository.findAllByCategory(category);
        return new ResponseEntity<>(advertisements, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getPageOfSortedAdvertisementsByCategoryAndStatus(
            Category category,
            AdvertisementStatus status,
            Integer numberOfPages,
            Integer pageNumber
    ) {
        Pageable dynamicPage = PageRequest.of(pageNumber, numberOfPages, Sort.by("createdAt").descending());
        List<Advertisement> advertisementsPage = advertisementsRepository.findAllByCategoryAndStatus(category, status, dynamicPage);
        Long totalCount = advertisementsRepository.count();
        AdvertisementPageResponseModel advertisementPageResponseModel = new AdvertisementPageResponseModel(
                advertisementsPage,
                totalCount
        );
        return new ResponseEntity<>(advertisementPageResponseModel, HttpStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public ResponseEntity<?> getAdvertisementById(String category, String id) {
        switch (category) {
            case "ITEM":
                ItemAdvertisementEntity advertisement = itemAdvertisementRepository.getItemAdvertisementEntityById(id)
                        .orElseThrow(() -> new ApiRequestException(
                                String.format("Item with id %s does not exist", id)
                        ));

                UserInfo userInfo = advertisement.getCreatedBy().getUserInfo();
                ItemAdvertisementResponse response = new ItemAdvertisementResponse(
                        advertisement.getTitle(),
                        advertisement.getDescription(),
                        advertisement.getCreatedAt(),
                        advertisement.getDimensions(),
                        userInfo.getFirstName(),
                        userInfo.getLastName(),
                        userInfo.getPhoneNumber(),
                        userInfo.getCity()
                );

                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

            default:
                throw new ApiRequestException("Invalid category");
        }
    }

    @Override
    public ResponseEntity<?> getAllAdvertisementsByEmail(String email) {
        List<Advertisement> advertisements = advertisementsRepository.findAllByCreatedByEmail(email);
        return new ResponseEntity<>(advertisements, HttpStatus.ACCEPTED);
    }
}
