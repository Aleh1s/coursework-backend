package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.request.UpdateUserRequest;
import ua.palamar.courseworkbackend.entity.image.Image;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
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
    @Autowired
    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<?> updateUser(
            @RequestBody UpdateUserRequest updateUserRequest,
            HttpServletRequest request
    ) {
        return userService.updateUser(updateUserRequest, request);
    }
}
