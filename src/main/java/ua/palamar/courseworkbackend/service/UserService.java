package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.request.UpdateUserRequest;
import ua.palamar.courseworkbackend.entity.user.UserAccount;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    UserAccount getUserEntityByEmail(String email);

    ResponseEntity<?> addImage(MultipartFile image, HttpServletRequest request);

    ResponseEntity<?> updateUser(UpdateUserRequest updateUserRequest, HttpServletRequest request);

    ResponseEntity<?> imageExists(String email);
}
