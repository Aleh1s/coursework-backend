package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.AdvertisementModel;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.GeneralizedAdvertisementService;

@RestController
@RequestMapping("/api/v1/advertisements")
@CrossOrigin("http://localhost:3000")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final GeneralizedAdvertisementService generalizedAdvertisementService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService,
                                   GeneralizedAdvertisementService generalizedAdvertisementService) {
        this.advertisementService = advertisementService;
        this.generalizedAdvertisementService = generalizedAdvertisementService;
    }

    @PostMapping
    public ResponseEntity<?> createAdvertisement(@RequestBody AdvertisementModel advertisementModel) {
        return advertisementService.saveAdvertisement(advertisementModel);
    }

    @DeleteMapping("/{category}/{id}")
    public ResponseEntity<?> removeAdvertisement(@PathVariable String category, @PathVariable String id) {
        return advertisementService.removeAdvertisement(category, id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getAllByEmail(@PathVariable String email) {
        return generalizedAdvertisementService.getAllAdvertisementsByEmail(email);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getAllByCategory(@PathVariable Category category) {
        return generalizedAdvertisementService.getAllAdvertisementsByCategory(category);
    }

    @GetMapping("/page/{category}")
    public ResponseEntity<?> getSortedPageByCreatedAtByCategory(
            @RequestParam Integer limit,
            @RequestParam Integer page,
            @RequestParam AdvertisementStatus status,
            @PathVariable Category category
    ) {
        return generalizedAdvertisementService.getPageOfSortedAdvertisementsByCategoryAndStatus(category, status, limit, page);
    }
}
