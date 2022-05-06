package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.AdvertisementModel;

public interface AdvertisementService {

    ResponseEntity<?> saveAdvertisement(AdvertisementModel advertisementModel);

//    ResponseEntity<?> updateAdvertisement();
//
    ResponseEntity<?> removeAdvertisement(String id);

}
