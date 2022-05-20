package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.service.AdvertisementService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/advertisements")
@CrossOrigin("http://localhost:3000")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
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
    public ResponseEntity<?> getAdvertisementResponseModelById(
            @RequestParam("_id") String id
    ) {
        return advertisementService.getAdvertisementResponseModelByAdvertisementId(id);
    }

    @GetMapping("/page")
    public ResponseEntity<?> getPage(
            @RequestParam("_limit") Integer limit,
            @RequestParam("_page") Integer page,
            @RequestParam("_category") Category category,
            @RequestParam("_sortBy") String sortBy
    ) {
        return advertisementService.getSortedPageByCategory(category, limit, page, sortBy);
    }

    @GetMapping("/email")
    public ResponseEntity<?> getAllByEmail(
        HttpServletRequest request
    ) {
        return advertisementService.getAllAdvertisementsByEmail(request);
    }

    @GetMapping("/query")
    public ResponseEntity<?> findAdvertisementsByCategoryAndTitleContainingOrDescriptionContaining(
            @RequestParam("_limit") Integer limit,
            @RequestParam("_page") Integer page,
            @RequestParam("_sortBy") String sortBy,
            @RequestParam("_query") String query,
            @RequestParam("_category") Category category
    ) {
        return advertisementService.findAdvertisementsByCategoryAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                category, query, sortBy, limit, page
        );
    }

}
