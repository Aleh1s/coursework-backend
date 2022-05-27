package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.entity.image.Image;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.service.ImageService;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public ImageController(
            ImageService imageService,
            UserService userService,
            UserRepository userRepository
    ) {
        this.imageService = imageService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    @GetMapping("/advertisements")
    public ResponseEntity<Object> getImageByAdvertisementId(@RequestParam("_advertisementId") String advertisementId) {
        Image image = imageService.getImageByAdvertisementId(advertisementId);
        if (image == null)
            return ResponseEntity.badRequest()
                    .body(null);

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUserProfileImage(
            @RequestParam("_image") MultipartFile image,
            HttpServletRequest request
    ) {
        return userService.addProfileImage(image, request);
    }

    @Transactional
    @GetMapping("/users")
    public ResponseEntity<Object> getUserProfileImageByEmail(
            @RequestParam("_email") String email
    ) {
        UserAccount user = userRepository.findUserEntityByEmailJoinFetchImage(email)
                .orElseThrow(() -> new ApiRequestException(
                        String.format(
                                "User with email %s does not exist", email
                        )
                ));

        Image image = user.getImage();

        if (image == null)
            return ResponseEntity.badRequest()
                    .body(null);

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @GetMapping("/users/exists")
    public ResponseEntity<?> imageExists(
            @RequestParam("_email") String email
    ) {
        return userService.imageExists(email);
    }
}
