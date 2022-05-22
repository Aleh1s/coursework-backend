package ua.palamar.courseworkbackend.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.request.UserDto;
import ua.palamar.courseworkbackend.entity.image.ImageEntity;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(
            UserService userService,
            UserRepository userRepository
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> addImage(
            @RequestParam("_image") MultipartFile image,
            HttpServletRequest request
    ) {
        return userService.addImage(image, request);
    }

    @Transactional
    @GetMapping("/image")
    public ResponseEntity<Object> getImageByEmail(@RequestParam("_email") String email) {
        UserEntity user = userRepository.findUserEntityByEmailJoinFetchImage(email)
                .orElseThrow(() -> new ApiRequestException(
                        String.format(
                                "User with email %s does not exist", email
                        )
                ));

        ImageEntity image = user.getImage();

        if (image == null)
            return ResponseEntity.badRequest()
                    .body(null);

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @PutMapping
    public ResponseEntity<?> updateUser(
            @RequestBody UserDto userDto,
            HttpServletRequest request
    ) {
        return userService.updateUser(userDto, request);
    }

    @GetMapping("/image/check")
    public ResponseEntity<?> imageExists(
            @RequestParam("_email") String email
    ) {
        return userService.imageExists(email);
    }
}
