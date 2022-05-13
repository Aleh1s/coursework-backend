package ua.palamar.courseworkbackend.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.adapter.ItemAdvertisementAdapter;
import ua.palamar.courseworkbackend.dto.response.AdvertisementPageResponseModel;
import ua.palamar.courseworkbackend.dto.response.ItemAdvertisementResponse;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.entity.user.UserInfo;
import ua.palamar.courseworkbackend.factory.AdvertisementFactory;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.AdvertisementsRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.ItemAdvertisementService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class SimpleAdvertisementService implements AdvertisementService {

    private final AdvertisementFactory advertisementFactory;
    private final ItemAdvertisementService itemAdvertisementService;
    private final AdvertisementsRepository advertisementsRepository;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final ItemAdvertisementAdapter itemAdvertisementAdapter;

    @Autowired
    public SimpleAdvertisementService(AdvertisementFactory advertisementFactory,
                                      ItemAdvertisementService itemAdvertisementService,
                                      AdvertisementsRepository advertisementsRepository,
                                      TokenProvider tokenProvider,
                                      UserService userService,
                                      ItemAdvertisementAdapter itemAdvertisementAdapter) {
        this.advertisementFactory = advertisementFactory;
        this.itemAdvertisementService = itemAdvertisementService;
        this.advertisementsRepository = advertisementsRepository;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.itemAdvertisementAdapter = itemAdvertisementAdapter;
    }

    @Override
    public Advertisement getByIdAndCategory(String id) {
        return advertisementsRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(
                        String.format("Advertisement with id %s does not exist", id)
                ));
    }

    @Override
    public ResponseEntity<?> save(AdvertisementRequestModel advertisementRequestModel, HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        String email = tokenProvider.getEmailByToken(token);
        Advertisement advertisement = advertisementFactory.createAdvertisement(advertisementRequestModel, email);
        return new ResponseEntity<>(advertisement, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> remove(String id, HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        String email = tokenProvider.getEmailByToken(token);

        UserEntity userEntity = userService.getUserEntityByEmail(email);

        Advertisement advertisement = advertisementsRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format("Advertisement with id %s does not exist", id)
                        )
                );

        boolean userHasAdvertisement = userEntity.getAdvertisements().contains(advertisement);

        if (userHasAdvertisement) {
            advertisementsRepository.deleteById(id);
        } else {
            throw new ApiRequestException(
                    String.format(
                            "User with email %s has no authorities to remove advertisement with id %s",
                            email,
                            id
                    )
            );
        }

        return new ResponseEntity<>("Post was successfully removed", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getAllByCategory(Category category) {
        List<Advertisement> advertisements = advertisementsRepository.findAllByCategory(category);
        return new ResponseEntity<>(advertisements, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getSortedPageByCategory(
            Category category,
            Integer numberOfPages,
            Integer pageNumber,
            String sortBy
    ) {
        Pageable dynamicPage = PageRequest.of(pageNumber, numberOfPages, Sort.by(sortBy).descending());
        List<Advertisement> advertisementsPage = advertisementsRepository.findAllByCategory(category, dynamicPage);
        Long totalCount = advertisementsRepository.count();
        AdvertisementPageResponseModel advertisementPageResponseModel = new AdvertisementPageResponseModel(
                advertisementsPage,
                totalCount
        );
        return new ResponseEntity<>(advertisementPageResponseModel, HttpStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public ResponseEntity<?> getByIdAndCategory(String category, String id) {
        switch (category) {
            case "ITEM":
                ItemAdvertisementEntity advertisement = itemAdvertisementService.getById(id);
                UserInfo userInfo = advertisement.getCreatedBy().getUserInfo();
                ItemAdvertisementResponse response =
                        itemAdvertisementAdapter.getResponse(advertisement, userInfo);
                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

            default:
                throw new ApiRequestException("Invalid category");
        }
    }

    @Override
    public ResponseEntity<?> getAllAdvertisementsByEmail(HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        List<Advertisement> advertisements = advertisementsRepository.findAllByCreatedByEmail(email);
        return new ResponseEntity<>(advertisements, HttpStatus.ACCEPTED);
    }

}