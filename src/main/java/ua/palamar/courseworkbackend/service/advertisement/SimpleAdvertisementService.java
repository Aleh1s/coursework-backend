package ua.palamar.courseworkbackend.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.dto.response.AdvertisementPageResponseModel;
import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.dto.response.UserResponseModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.image.ImageEntity;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.AdvertisementsRepository;
import ua.palamar.courseworkbackend.repository.ImageRepository;
import ua.palamar.courseworkbackend.repository.OrderRepository;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    private final ImageRepository imageRepository;

    @Autowired
    public SimpleAdvertisementService(
            AdvertisementsRepository advertisementsRepository,
            TokenProvider tokenProvider,
            UserService userService,
            UserRepository userRepository,
            OrderRepository orderRepository,
            ImageRepository imageRepository
    ) {
        this.advertisementsRepository = advertisementsRepository;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.imageRepository = imageRepository;
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
    public ResponseEntity<?> save(AdvertisementRequestModel advertisementRequestModel, HttpServletRequest request, MultipartFile file) {
        String email = tokenProvider.getEmail(request);

        UserEntity creator = userService.getUserEntityByEmail(email);

        ImageEntity image;

        if (file == null) {
            throw new ApiRequestException("The image must exists");
        }

        try {
            image = new ImageEntity(
                    file.getName(),
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageRepository.save(image);

        Advertisement advertisement = new Advertisement(
                advertisementRequestModel.title(),
                advertisementRequestModel.description(),
                advertisementRequestModel.category(),
                advertisementRequestModel.city(),
                image
        );

        advertisement.addCreator(creator);

        advertisementsRepository.save(advertisement);

        return new ResponseEntity<>(advertisement, HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<?> remove(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        UserEntity userEntity = userService.getUserEntityByEmail(email);

        Advertisement advertisement = advertisementsRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format("Advertisement with id %s does not exist", id)
                        )
                );

        boolean userIsOwner = userEntity.getAdvertisements().contains(advertisement);

        Set<OrderEntity> orders = advertisement.getOrderEntities();

        boolean hasConfirmedOrders = orders.stream()
                .anyMatch(order -> order.getOrderStatus().equals(OrderStatus.CONFIRMED));

        if (hasConfirmedOrders) {
            throw new ApiRequestException(
                    String.format(
                            "User with email %s has confirmed orders", email
                    )
            );
        }
        
        int count;
        if (userIsOwner) {
            advertisement.removeOrders(orders);
            advertisement.removeCreator(userEntity);
            count = advertisementsRepository.removeAdvertisementById(advertisement.getId());
        } else {
            throw new ApiRequestException(
                    String.format(
                            "User with email %s has no authorities to remove advertisement with id %s",
                            email,
                            id
                    )
            );
        }

        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<?> findAdvertisementsByCategoryAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(Category category, String query, String sortBy, Integer limit, Integer page) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortBy).descending());
        List<Advertisement> advertisements = advertisementsRepository.findAdvertisementsByCategoryAndTitleContainingIgnoreCase(category, query, pageable);
        Long totalCount = advertisementsRepository.countAdvertisementsByCategoryAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(category, query, query);
        AdvertisementPageResponseModel responseModel = new AdvertisementPageResponseModel(
                advertisements,
                totalCount
        );
        return new ResponseEntity<>(responseModel, HttpStatus.ACCEPTED);
    }

    @Override
    public ImageEntity getImageById(String id) {
        Advertisement advertisement = advertisementsRepository.findAdvertisementByIdJoinFetchImage(id)
                .orElseThrow(() -> new ApiRequestException(
                        String.format(
                                "Advertisement with id %s does not exist", id
                        )
                ));

        return advertisement.getImage();
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
                advertisement.getCity(),
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
                        advertisement.getCity(),
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