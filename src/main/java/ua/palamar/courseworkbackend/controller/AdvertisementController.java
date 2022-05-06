package ua.palamar.courseworkbackend.controller;

import com.sun.net.httpserver.HttpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.AdvertisementModel;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;
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
            @RequestBody AdvertisementModel advertisementModel,
            HttpServletRequest request
    ) {
        return advertisementService.saveAdvertisement(advertisementModel, request);
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
            @RequestParam("_limit") Integer limit,
            @RequestParam("_page") Integer page,
            @RequestParam("_status") AdvertisementStatus status,
            @PathVariable Category category
    ) {
        return generalizedAdvertisementService.getPageOfSortedAdvertisementsByCategoryAndStatus(category, status, limit, page);
    }
}
