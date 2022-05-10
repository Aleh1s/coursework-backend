package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AdvertisementService {

    ResponseEntity<?> saveAdvertisement(AdvertisementRequestModel advertisementRequestModel, HttpServletRequest request);

//    ResponseEntity<?> updateAdvertisement();

    ResponseEntity<?> removeAdvertisement(String id, HttpServletRequest request);
}
