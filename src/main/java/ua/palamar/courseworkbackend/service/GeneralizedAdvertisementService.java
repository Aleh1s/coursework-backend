package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;

public interface GeneralizedAdvertisementService {

    ResponseEntity<?> getAllAdvertisementsByEmail(String email);

}
