package ua.palamar.courseworkbackend.service.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.request.RegistrationRequestModel;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.service.RegistrationService;
import ua.palamar.courseworkbackend.service.UserService;
import ua.palamar.courseworkbackend.service.UserServiceValidator;
import ua.palamar.courseworkbackend.validator.RegistrationValidator;

@Service
public class SimpleRegistrationService implements RegistrationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserServiceValidator userServiceValidator;
    private final RegistrationValidator registrationValidator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SimpleRegistrationService(
            UserService userService,
            UserRepository userRepository,
            UserServiceValidator userServiceValidator,
            RegistrationValidator registrationValidator,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userServiceValidator = userServiceValidator;
        this.registrationValidator = registrationValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> register(RegistrationRequestModel registrationRequestModel) {

        if (userServiceValidator.userWithEmailExists(registrationRequestModel.email()))
            throw new ApiRequestException(
                    String.format("User with email: %s already exists", registrationRequestModel.email())
            );

        if (userServiceValidator.userWithPhoneNumberExists(registrationRequestModel.phoneNumber()))
            throw new ApiRequestException(
                    String.format("User with phone number: %s already exists", registrationRequestModel.phoneNumber())
            );

        if (!registrationValidator.isEmailValid(registrationRequestModel.email()))
            throw new IllegalStateException("Invalid email");

        if (!registrationValidator.isPasswordValid(registrationRequestModel.password()))
            throw new IllegalStateException("Invalid password");

        if (!registrationValidator.isFirstNameValid(registrationRequestModel.firstName()))
            throw new IllegalStateException("Invalid first name");

        if (!registrationValidator.isLastNameValid(registrationRequestModel.lastName()))
            throw new IllegalStateException("Invalid last name");

        if (!registrationValidator.isPhoneNumberValid(registrationRequestModel.phoneNumber()))
            throw new IllegalStateException("Invalid phone number");

        UserEntity newUser = new UserEntity(
                registrationRequestModel.email(),
                passwordEncoder.encode(registrationRequestModel.password()),
                registrationRequestModel.firstName(),
                registrationRequestModel.lastName(),
                UserRole.USER,
                registrationRequestModel.phoneNumber()
        );

        userRepository.save(newUser);

        return new ResponseEntity<>("Successful registration", HttpStatus.CREATED);
    }
}
