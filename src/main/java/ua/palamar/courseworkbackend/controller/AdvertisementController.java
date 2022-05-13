package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.GeneralizedAdvertisementService;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<?> createAdvertisement(
            @RequestBody AdvertisementRequestModel advertisementRequestModel,
            HttpServletRequest request
    ) {
        return advertisementService.save(advertisementRequestModel, request);
    }

    @DeleteMapping
    public ResponseEntity<?> removeAdvertisement(
            @RequestParam("_id") String id,
            HttpServletRequest request
    ) {
        return advertisementService.remove(id, request);
    }
    @GetMapping
    public ResponseEntity<?> getAdvertisementById(
            @RequestParam("_category") String category,
            @RequestParam("_id") String id
    ) {
        return generalizedAdvertisementService.getAdvertisementById(category, id);
    }

    @GetMapping("/page")
    public ResponseEntity<?> getSortedPageByCreatedAtByCategory(
            @RequestParam("_limit") Integer limit,
            @RequestParam("_page") Integer page,
            @RequestParam("_status") ItemAdvertisementStatus status,
            @RequestParam("_category") Category category
    ) {
        return generalizedAdvertisementService.getPageOfSortedAdvertisementsByCategoryAndStatus(category, status, limit, page);
    }
}
