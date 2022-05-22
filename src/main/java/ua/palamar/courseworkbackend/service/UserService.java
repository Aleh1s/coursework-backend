package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.request.UserDto;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    UserEntity getUserEntityById(String id);
    UserEntity getUserEntityByEmail(String email);

    ResponseEntity<?> addImage(MultipartFile image, HttpServletRequest request);

    ResponseEntity<?> updateUser(UserDto userDto, HttpServletRequest request);

    ResponseEntity<?> imageExists(String email);
}
