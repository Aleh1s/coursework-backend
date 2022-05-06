package ua.palamar.courseworkbackend.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.creator.AdvertisementCreator;
import ua.palamar.courseworkbackend.dto.AdvertisementModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.ItemAdvertisementRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.AdvertisementService;

import javax.servlet.http.HttpServletRequest;

@Service
public class SimpleAdvertisementService implements AdvertisementService {

    private final AdvertisementCreator advertisementCreator;
    private final ItemAdvertisementRepository itemAdvertisementRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    public SimpleAdvertisementService(AdvertisementCreator advertisementCreator,
                                      ItemAdvertisementRepository itemAdvertisementRepository,
                                      TokenProvider tokenProvider) {
        this.advertisementCreator = advertisementCreator;
        this.itemAdvertisementRepository = itemAdvertisementRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public ResponseEntity<?> saveAdvertisement(AdvertisementModel advertisementModel, HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        String email = tokenProvider.getEmailByToken(token);
        Advertisement advertisement = advertisementCreator.createAdvertisement(advertisementModel, email);
        return new ResponseEntity<>(advertisement, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> removeAdvertisement(String category,String id) {
        switch (category) {
            case "ITEM":
                itemAdvertisementRepository.deleteById(id);
                break;
            case "SERVICE":
                // todo: implement delete
                break;
            case "HOUSE":
                // todo: implement delete
                break;
            default:
                throw new ApiRequestException(
                        String.format("Invalid category %s", category)
                );
        }
        return new ResponseEntity<>("Post was successfully removed", HttpStatus.ACCEPTED);
    }
}
