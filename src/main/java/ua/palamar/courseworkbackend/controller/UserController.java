package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.criteria.AdvertisementCriteria;
import ua.palamar.courseworkbackend.dto.request.UpdateUserRequest;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsDetailsResponse;
import ua.palamar.courseworkbackend.service.AdvertisementService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final AdvertisementService advertisementService;
    @Autowired
    public UserController(
            UserService userService,
            AdvertisementService advertisementService
    ) {
        this.userService = userService;
        this.advertisementService = advertisementService;
    }

    @PatchMapping
    public ResponseEntity<?> updateUser(
            @RequestBody UpdateUserRequest updateUserRequest,
            HttpServletRequest request
    ) {
        return userService.updateUser(updateUserRequest, request);
    }

    @GetMapping("/advertisements")
    public ResponseEntity<AdvertisementsDetailsResponse> getAdvertisementsByEmail(
            @RequestParam(value = "_limit", defaultValue = "12") Integer limit,
            @RequestParam(value = "_page", defaultValue = "0") Integer page,
            @RequestParam(value = "_sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam("_email") String email
    ) {
        return new ResponseEntity<>(advertisementService.getAllByEmailAndCriteria(email, new AdvertisementCriteria(null, limit, page, sortBy, null)
        ), HttpStatus.OK);
    }
}
