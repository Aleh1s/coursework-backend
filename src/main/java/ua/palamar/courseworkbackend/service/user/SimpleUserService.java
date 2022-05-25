package ua.palamar.courseworkbackend.service.user;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.request.UserDto;
import ua.palamar.courseworkbackend.entity.image.ImageEntity;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.ImageRepository;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.UserService;
import ua.palamar.courseworkbackend.service.UserServiceValidator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@Service
public class SimpleUserService implements UserService, UserServiceValidator {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final TokenProvider tokenProvider;
    public SimpleUserService(
            UserRepository userRepository,
            ImageRepository imageRepository,
            @Lazy TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public UserEntity getUserEntityByEmail(String email) {
        return userRepository.getUserEntityByEmail(email)
                .orElseThrow(() -> new ApiRequestException(
                        String.format("User with email: %s does not exist", email))
                );
    }

    @Override
    @Transactional
    public ResponseEntity<?> addImage(MultipartFile file, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        UserEntity user = getUserEntityByEmail(email);

        ImageEntity image;

        if (file.getSize() != 0) {
            try {
                image = new ImageEntity(
                        file.getName(),
                        file.getOriginalFilename(),
                        file.getSize(),
                        file.getContentType(),
                        file.getBytes()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            user.setImage(image);
            imageRepository.save(image);
        }



        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> updateUser(UserDto userDto, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        UserEntity user = getUserEntityByEmail(email);

        String phoneNumber = userDto.phoneNumber();
        if (!phoneNumber.equals("")) {
            if (phoneNumber.startsWith("+38")) {
                phoneNumber = phoneNumber.substring("+38".length());
            }

            if (!phoneNumber.equals(user.getPhoneNumber()) && userRepository.existsByPhoneNumber(phoneNumber)) {
                throw new ApiRequestException(
                        String.format("User with phone number %s already exists", phoneNumber)
                );
            }

            user.setPhoneNumber(phoneNumber);
        }

        String firstName = userDto.firstName();
        if (!firstName.equals("")) {
            user.setFirstName(firstName);
        }

        String lastName = userDto.lastName();
        if (!lastName.equals("")) {
            user.setLastName(lastName);
        }

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> imageExists(String email) {
        UserEntity user = getUserEntityByEmail(email);

        if (user.getImage() != null) {
            return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(false, HttpStatus.ACCEPTED);
    }

    @Override
    public boolean userWithEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean userWithPhoneNumberExists(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
