package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.criteria.AdvertisementCriteria;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequest;
import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsDetailsResponse;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsResponse;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.service.AdvertisementService;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

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
    public ResponseEntity<AdvertisementResponse> create(
            @RequestParam("_image") MultipartFile file,
            @RequestParam("_title") String title,
            @RequestParam("_description") String description,
            @RequestParam("_city") String city,
            @RequestParam("_category") Category category,
            HttpServletRequest request
    ) {
        AdvertisementRequest model = new AdvertisementRequest(title, description, category, city);
        return new ResponseEntity<>(advertisementService.save(model, request, file), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> removeById(
            @RequestParam("_id") String id,
            HttpServletRequest request
    ) {
        advertisementService.remove(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AdvertisementResponse> getById(@PathVariable("id") String id) {
        return new ResponseEntity<>(advertisementService.getByIdJoinFetchCreator(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AdvertisementsResponse> getAll(
            @RequestParam(value = "_limit", defaultValue = "12") Integer limit,
            @RequestParam(value = "_page", defaultValue = "0") Integer page,
            @RequestParam(value = "_category", defaultValue = "ITEM") Category category,
            @RequestParam(value = "_sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "_query", defaultValue = "") String query
    ) {
        return new ResponseEntity<>(
                advertisementService.getAllByCriteria(
                        new AdvertisementCriteria(category, limit, page, sortBy, query)
                ), HttpStatus.OK);
    }

    @GetMapping("/one")
    public ResponseEntity<AdvertisementsDetailsResponse> getAllByEmail(
            @RequestParam(value = "_limit", defaultValue = "12") Integer limit,
            @RequestParam(value = "_page", defaultValue = "0") Integer page,
            @RequestParam(value = "_sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam("_email") String email
    ) {
        return new ResponseEntity<>(advertisementService.getAllByEmail(email, new AdvertisementCriteria(null, limit, page, sortBy, null)
        ), HttpStatus.OK);
    }
}
