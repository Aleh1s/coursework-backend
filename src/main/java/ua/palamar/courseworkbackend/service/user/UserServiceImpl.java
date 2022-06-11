package ua.palamar.courseworkbackend.service.user;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.adapter.user.UserDtoAdapter;
import ua.palamar.courseworkbackend.dto.request.UpdateUserRequest;
import ua.palamar.courseworkbackend.dto.response.UserResponse;
import ua.palamar.courseworkbackend.entity.image.Image;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.ImageRepository;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.security.Jwt.TokenProvider;
import ua.palamar.courseworkbackend.service.UserService;
import ua.palamar.courseworkbackend.service.UserServiceValidator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class UserServiceImpl implements UserService, UserServiceValidator {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final TokenProvider tokenProvider;
    private final UserDtoAdapter userDtoAdapter;

    public UserServiceImpl(
            UserRepository userRepository,
            ImageRepository imageRepository,
            @Lazy TokenProvider tokenProvider,
            UserDtoAdapter userDtoAdapter
    ) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.tokenProvider = tokenProvider;
        this.userDtoAdapter = userDtoAdapter;
    }

    @Override
    public UserAccount getUserEntityByEmail(String email) {
        return userRepository.getUserEntityByEmail(email)
                .orElseThrow(() -> new ApiRequestException(
                        String.format("User with email: %s does not exist", email))
                );
    }

    @Override
    @Transactional
    public ResponseEntity<?> addProfileImage(MultipartFile file, HttpServletRequest request) {
        String email = tokenProvider.getEmail(request);
        UserAccount user = getUserEntityByEmail(email);

        Image image;

        if (file.getSize() != 0) {
            try {
                image = new Image(
                        file.getName(),
                        file.getOriginalFilename(),
                        file.getSize(),
                        file.getContentType(),
                        file.getBytes()
                );
            } catch (IOException e) {
                throw new ApiRequestException("Can not create image", e);
            }

            user.setImage(image);
            imageRepository.save(image);
        } else {
            throw new ApiRequestException("Image must exists");
        }



        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> updateUser(
            UpdateUserRequest updateUserRequest,
            HttpServletRequest request
    ) {
        String email = tokenProvider.getEmail(request);
        UserAccount user = getUserEntityByEmail(email);

        String phoneNumber = updateUserRequest.phoneNumber().trim();
        if (!phoneNumber.isBlank()) {
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

        String firstName = updateUserRequest.firstName().trim();
        if (!firstName.isBlank()) {
            user.setFirstName(firstName);
        }

        String lastName = updateUserRequest.lastName().trim();
        if (!lastName.isBlank()) {
            user.setLastName(lastName);
        }

        UserAccount updatedUser = userRepository.save(user);
        UserResponse response = userDtoAdapter.getModel(updatedUser);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<?> imageExists(String email) {
        UserAccount user = getUserEntityByEmail(email);

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
