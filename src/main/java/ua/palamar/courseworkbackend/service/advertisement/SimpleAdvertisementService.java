package ua.palamar.courseworkbackend.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.factory.AdvertisementFactory;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.AdvertisementsRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Service
public class SimpleAdvertisementService implements AdvertisementService {

    private final AdvertisementFactory advertisementFactory;
    private final AdvertisementsRepository advertisementsRepository;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public SimpleAdvertisementService(AdvertisementFactory advertisementFactory,
                                      AdvertisementsRepository advertisementsRepository,
                                      TokenProvider tokenProvider,
                                      UserService userService) {
        this.advertisementFactory = advertisementFactory;
        this.advertisementsRepository = advertisementsRepository;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<?> saveAdvertisement(AdvertisementRequestModel advertisementRequestModel, HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        String email = tokenProvider.getEmailByToken(token);
        Advertisement advertisement = advertisementFactory.createAdvertisement(advertisementRequestModel, email);
        return new ResponseEntity<>(advertisement, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> removeAdvertisement(String id, HttpServletRequest request) {
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
}
