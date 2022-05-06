package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.palamar.courseworkbackend.dto.AdvertisementModel;
import ua.palamar.courseworkbackend.service.AdvertisementService;

@RestController
@RequestMapping("/api/v1/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostMapping
    public ResponseEntity<?> createAdvertisement(@RequestBody AdvertisementModel advertisementModel) {
        return advertisementService.saveAdvertisement(advertisementModel);
    }
}
