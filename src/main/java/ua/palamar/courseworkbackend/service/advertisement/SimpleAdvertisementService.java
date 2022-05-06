package ua.palamar.courseworkbackend.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.creator.AdvertisementCreator;
import ua.palamar.courseworkbackend.dto.AdvertisementModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.service.AdvertisementService;

@Service
public class SimpleAdvertisementService implements AdvertisementService {

    private final AdvertisementCreator advertisementCreator;

    @Autowired
    public SimpleAdvertisementService(AdvertisementCreator advertisementCreator) {
        this.advertisementCreator = advertisementCreator;
    }

    @Override
    public ResponseEntity<?> saveAdvertisement(AdvertisementModel advertisementModel) {
        Advertisement advertisement = advertisementCreator.createAdvertisement(advertisementModel);
        return new ResponseEntity<>(advertisement, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> removeAdvertisement(String id) {
        return null;
    }
}
