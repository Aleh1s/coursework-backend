package ua.palamar.courseworkbackend.service.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.request.RegistrationRequest;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.service.RegistrationService;
import ua.palamar.courseworkbackend.service.UserServiceValidator;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final UserServiceValidator userServiceValidator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationServiceImpl(
            UserRepository userRepository,
            UserServiceValidator userServiceValidator,
            PasswordEncoder passwordEncoder) {
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
        if (userServiceValidator.userWithPhoneNumberExists(phoneNumber))
            throw new ApiRequestException(
                    String.format("User with phone number: %s already exists", phoneNumber)
            );

        String email = registrationRequest.email().trim();
        String firstName= registrationRequest.firstName().trim();
        String lastName= registrationRequest.lastName().trim();

        UserAccount newUser = new UserAccount(
                email,
                passwordEncoder.encode(registrationRequest.password()),
                firstName,
                lastName,
                UserRole.USER,
                phoneNumber
        );

        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
