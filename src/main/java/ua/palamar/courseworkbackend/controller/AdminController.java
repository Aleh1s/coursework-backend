package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.criteria.AdvertisementCriteria;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsResponse;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;
import ua.palamar.courseworkbackend.entity.user.UserStatus;
import ua.palamar.courseworkbackend.service.AdminService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin("http://localhost:3000")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/advertisements")
    public ResponseEntity<AdvertisementsResponse> getAdvertisementsForModeration(
            @RequestParam(value = "_limit", defaultValue = "12") Integer limit,
            @RequestParam(value = "_page", defaultValue = "0") Integer page,
            @RequestParam(value = "_sortBy", defaultValue = "") String sortBy
    ) {
        return new ResponseEntity<>(
                adminService.getAdvertisementsForModeration(
                        new AdvertisementCriteria(null, limit, page, sortBy, null)
                ), HttpStatus.OK
        );
    }

    @PatchMapping("/users")
    public ResponseEntity<?> changeUserAccountStatus(
            @RequestParam("_email") String email,
            @RequestParam("_status") UserStatus status
    ) {
        adminService.changeUserAccountStatus(email, status);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/advertisements")
    public ResponseEntity<?> changeAdvertisementStatus(
            @RequestParam("_id") String id,
            @RequestParam("_status") AdvertisementStatus status,
            HttpServletRequest request
    ) {
        adminService.changeAdvertisementStatus(id, status, request);
        return ResponseEntity.noContent().build();
    }

}
