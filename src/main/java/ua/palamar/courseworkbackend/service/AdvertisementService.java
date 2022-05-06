package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.AdvertisementModel;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AdvertisementService {

    ResponseEntity<?> saveAdvertisement(AdvertisementModel advertisementModel, HttpServletRequest request);

//    ResponseEntity<?> updateAdvertisement();
//
    ResponseEntity<?> removeAdvertisement(String category,String id);

}
