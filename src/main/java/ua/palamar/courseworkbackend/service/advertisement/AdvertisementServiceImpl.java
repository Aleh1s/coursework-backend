package ua.palamar.courseworkbackend.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.adapter.advertisement.AdvertisementDtoAdapter;
import ua.palamar.courseworkbackend.adapter.user.UserDtoAdapter;
import ua.palamar.courseworkbackend.dto.criteria.AdvertisementCriteria;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequest;
import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsDetailsResponse;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsResponse;
import ua.palamar.courseworkbackend.dto.response.UserResponse;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementCategory;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;
import ua.palamar.courseworkbackend.entity.image.Image;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;
import ua.palamar.courseworkbackend.entity.order.OrderStatus;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.AdvertisementRepository;
import ua.palamar.courseworkbackend.repository.ImageRepository;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final UserDtoAdapter userDtoAdapter;
    private final AdvertisementDtoAdapter advertisementDtoAdapter;

    @Autowired
    public AdvertisementServiceImpl(
            AdvertisementRepository advertisementRepository,
            TokenProvider tokenProvider,
            UserService userService,
            UserRepository userRepository,
            ImageRepository imageRepository,
            UserDtoAdapter userDtoAdapter,
            AdvertisementDtoAdapter advertisementDtoAdapter) {
        this.advertisementRepository = advertisementRepository;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.userDtoAdapter = userDtoAdapter;
        this.advertisementDtoAdapter = advertisementDtoAdapter;
    }

    @Override
    @Transactional
    public AdvertisementResponse save(AdvertisementRequest advertisementRequest, HttpServletRequest request, MultipartFile file) {
        String email = tokenProvider.getEmail(request);

        UserAccount creator = userService.getUserEntityByEmail(email);

        Image image;

        if (!Objects.nonNull(file)) {
            throw new ApiRequestException("The image must exists");
        }

        try {
            image = new Image(
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
                advertisementRequest.title(),
                advertisementRequest.description(),
                advertisementRequest.category(),
                advertisementRequest.city(),
                image
        );

        advertisement.addCreator(creator);

        advertisementRepository.save(advertisement);

        return advertisementDtoAdapter.getModel(advertisement, creator);
    }

    @Override
    @Transactional
    public void remove(String id, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);

        UserAccount userAccount = userService.getUserEntityByEmail(email);

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format("Advertisement with id %s does not exist", id)
                        )
                );

        boolean userIsOwner = userAccount.getAdvertisements().contains(advertisement);

        Set<OrderEntity> orderEntities = advertisement.getOrders();

        boolean hasConfirmedOrders = orderEntities.stream()
                .anyMatch(order -> order.getOrderStatus().equals(OrderStatus.CONFIRMED));

        if (hasConfirmedOrders) {
            throw new ApiRequestException(
                    String.format(
                            "User with email %s has confirmed orders", email
                    )
            );
        }

        if (userIsOwner) {
            advertisement.removeOrders(orderEntities);
            advertisement.removeCreator(userAccount);
            advertisementRepository.removeAdvertisementById(advertisement.getId());
        } else {
            throw new ApiRequestException(
                    String.format(
                            "User with email %s has no authorities to remove advertisement with id %s",
                            email,
                            id
                    )
            );
        }
    }

    @Override
    public AdvertisementsResponse getAllByCriteria(
            AdvertisementCriteria criteria
    ) {
        Pageable pageable = PageRequest.of(criteria.page(), criteria.limit(), Sort.by(criteria.sortBy()).descending());

        String query = criteria.query();
        AdvertisementCategory category = criteria.category();

        List<Advertisement> advertisements = advertisementRepository
                .findAdvertisementsByCategoryAndStatusAndTitleContainingIgnoreCase(category, AdvertisementStatus.CHECKED, query, pageable);
        Long count = advertisementRepository
                .countAdvertisementsByCategoryAndStatusAndTitleContainingIgnoreCase(category, AdvertisementStatus.CHECKED, query);

        return new AdvertisementsResponse(
                advertisements,
                count
        );
    }

    @Override
    public AdvertisementResponse getByIdJoinFetchCreator(String id) {
        Advertisement advertisement = advertisementRepository.findAdvertisementByIdJoinFetchCreator(id)
                .orElseThrow(() -> new ApiRequestException(
                                String.format(
                                        "Advertisement with id %s does not exist", id)
                        )
                );

        UserAccount creator = advertisement.getCreator();
        UserResponse userResponse = userDtoAdapter.getModel(creator);

        return new AdvertisementResponse(
                advertisement.getId(),
                advertisement.getTitle(),
                advertisement.getDescription(),
                advertisement.getCity(),
                advertisement.getStatus(),
                advertisement.getCategory(),
                advertisement.getCreatedAt(),
                userResponse
        );
    }

    @Override
    public AdvertisementsDetailsResponse getAllByEmail(String email, AdvertisementCriteria criteria) {
        UserAccount user = userRepository.findUserEntityByEmailJoinFetchAdvertisements(email)
                .orElseThrow(() -> new ApiRequestException(
                                String.format(
                                        "User with email does not exist", email
                                )
                        )
                );

        Pageable pageable = PageRequest.of(criteria.page(), criteria.limit(), Sort.by(criteria.sortBy()).descending());

        List<Advertisement> advertisements = advertisementRepository.findAdvertisementsByCreatorEmail(email, pageable);
        Long count = advertisementRepository.countAdvertisementsByCreatorEmail(email);

        UserResponse userResponse = userDtoAdapter.getModel(user);

        return new AdvertisementsDetailsResponse(getAdvertisementResponses(advertisements, userResponse), count);
    }

    private List<AdvertisementResponse> getAdvertisementResponses(List<Advertisement> advertisements, UserResponse userResponse) {
        return advertisements.stream()
                .map(advertisement -> new AdvertisementResponse(
                        advertisement.getId(),
                        advertisement.getTitle(),
                        advertisement.getDescription(),
                        advertisement.getCity(),
                        advertisement.getStatus(),
                        advertisement.getCategory(),
                        advertisement.getCreatedAt(),
                        userResponse
                )).collect(Collectors.toList());
    }



}