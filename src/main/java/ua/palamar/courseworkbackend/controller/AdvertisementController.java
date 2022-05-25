package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.entity.image.ImageEntity;
import ua.palamar.courseworkbackend.service.AdvertisementService;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;

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
            @RequestParam("_image") MultipartFile file,
            @RequestParam("_title") String title,
            @RequestParam("_description") String description,
            @RequestParam("_city") String city,
            @RequestParam("_category") Category category,
            HttpServletRequest request
    ) {
        AdvertisementRequestModel dto = new AdvertisementRequestModel(
                title,
                description,
                category,
                city
        );
        return advertisementService.save(dto, request, file);
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

    @Transactional
    @GetMapping("/image")
    public ResponseEntity<Object> getImageById(@RequestParam("_id") String id) {
        ImageEntity image = advertisementService.getImageById(id);
        if (image == null)
            return ResponseEntity.badRequest()
                    .body(null);

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

}
