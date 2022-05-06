package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.AdvertisementModel;

@Service
public interface AdvertisementService {

    ResponseEntity<?> saveAdvertisement(AdvertisementModel advertisementModel);

//    ResponseEntity<?> updateAdvertisement();
//
    ResponseEntity<?> removeAdvertisement(String category,String id);

}
