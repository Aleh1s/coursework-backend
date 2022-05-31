package ua.palamar.courseworkbackend.service.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.request.RegistrationRequest;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.entity.user.UserStatus;
import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.service.RegistrationService;
import ua.palamar.courseworkbackend.service.UserService;
import ua.palamar.courseworkbackend.service.UserServiceValidator;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserServiceValidator userServiceValidator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationServiceImpl(
            UserService userService,
            UserRepository userRepository,
            UserServiceValidator userServiceValidator,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userServiceValidator = userServiceValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> register(RegistrationRequest registrationRequest) {

        if (userServiceValidator.userWithEmailExists(registrationRequest.email()))
            throw new ApiRequestException(
                    String.format("User with email: %s already exists", registrationRequest.email())
            );

        String phoneNumber = registrationRequest.phoneNumber();

        if (phoneNumber.startsWith("+38"))
            phoneNumber = phoneNumber.substring("+38".length());


        if (userServiceValidator.userWithPhoneNumberExists(phoneNumber))
            throw new ApiRequestException(
                    String.format("User with phone number: %s already exists", phoneNumber)
            );

        UserAccount newUser = new UserAccount(
                registrationRequest.email(),
                passwordEncoder.encode(registrationRequest.password()),
                registrationRequest.firstName(),
                registrationRequest.lastName(),
                UserRole.USER,
                phoneNumber
        );

        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
