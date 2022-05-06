package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping("/{category}/{id}")
    public ResponseEntity<?> removeAdvertisement(@PathVariable String category, @PathVariable String id) {
        return advertisementService.removeAdvertisement(category, id);
    }
}
