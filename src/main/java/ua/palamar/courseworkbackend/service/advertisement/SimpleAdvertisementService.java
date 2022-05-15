package ua.palamar.courseworkbackend.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.dto.response.AdvertisementPageResponseModel;
import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.dto.response.UserResponseModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.AdvertisementsRepository;
import ua.palamar.courseworkbackend.repository.OrderRepository;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SimpleAdvertisementService implements AdvertisementService {

    private final AdvertisementsRepository advertisementsRepository;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public SimpleAdvertisementService(
            AdvertisementsRepository advertisementsRepository,
            TokenProvider tokenProvider,
            UserService userService,
            UserRepository userRepository,
            OrderRepository orderRepository
    ) {
        this.advertisementsRepository = advertisementsRepository;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Advertisement getAdvertisementById(String id) {
        return advertisementsRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(
                        String.format("Advertisement with id %s does not exist", id)
                ));
    }

    @Override
    @Transactional
    public ResponseEntity<?> save(AdvertisementRequestModel advertisementRequestModel, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        UserEntity creator = userService.getUserEntityByEmail(email);

        Advertisement advertisement = new Advertisement(
                advertisementRequestModel.title(),
                advertisementRequestModel.description(),
                advertisementRequestModel.category()
        );

        advertisement.addCreator(creator);

        advertisementsRepository.save(advertisement);

        return new ResponseEntity<>(advertisement, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> remove(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        UserEntity userEntity = userService.getUserEntityByEmail(email);

        Advertisement advertisement = advertisementsRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format("Advertisement with id %s does not exist", id)
                        )
                );

        boolean userHasAdvertisement = userEntity.getAdvertisements().contains(advertisement);

        if (userHasAdvertisement) {
            advertisementsRepository.delete(advertisement);
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
    public ResponseEntity<?> getAdvertisementResponseModelByAdvertisementId(String id) {
        Advertisement advertisement = advertisementsRepository.findAdvertisementByIdJoinFetchCreator(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format(
                                        "Advertisement with id %s does not exist", id)
                        )
                );

        UserEntity creator = advertisement.getCreator();

        UserResponseModel userResponseModel = new UserResponseModel(
                creator.getEmail(),
                creator.getFirstName(),
                creator.getLastName(),
                creator.getPhoneNumber()
        );

        AdvertisementResponse response = new AdvertisementResponse(
                advertisement.getId(),
                advertisement.getTitle(),
                advertisement.getDescription(),
                advertisement.getCategory(),
                advertisement.getCreatedAt(),
                userResponseModel
        );

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getAllAdvertisementsByEmail(HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        UserEntity user = userRepository.findUserEntityByEmailJoinFetchAdvertisements(email)
                .orElseThrow(() -> new ApiRequestException(
                                String.format(
                                        "User with email does not exist", email
                                )
                        )
                );

        Set<AdvertisementResponse> responses = user.getAdvertisements().stream()
                .map(advertisement -> new AdvertisementResponse(
                        advertisement.getId(),
                        advertisement.getTitle(),
                        advertisement.getDescription(),
                        advertisement.getCategory(),
                        advertisement.getCreatedAt(),
                        new UserResponseModel(
                                user.getEmail(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getPhoneNumber()
                        )
                )).collect(Collectors.toSet());

        return new ResponseEntity<>(responses, HttpStatus.ACCEPTED);
    }

}