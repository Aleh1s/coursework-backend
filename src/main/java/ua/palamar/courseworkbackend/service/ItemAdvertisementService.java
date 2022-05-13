package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;

public interface ItemAdvertisementService {

    void removeById(String id);
    ItemAdvertisementEntity getById(String id);

}
