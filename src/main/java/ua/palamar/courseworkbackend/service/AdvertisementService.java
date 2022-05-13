package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AdvertisementService {

    Advertisement getById(String id);
    ResponseEntity<?> save(AdvertisementRequestModel advertisementRequestModel, HttpServletRequest request);

//    ResponseEntity<?> updateAdvertisement();

    ResponseEntity<?> remove(String id, HttpServletRequest request);
}
