package ua.palamar.courseworkbackend.service.user;

import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.service.UserService;
import ua.palamar.courseworkbackend.service.UserServiceValidator;

@Service
public class SimpleUserService implements UserService, UserServiceValidator {

    private final UserRepository userRepository;

    public SimpleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getUserEntityById(String id) {
        return null;
    }

    @Override
    public UserEntity getUserEntityByEmail(String email) {
        return userRepository.getUserEntityByEmail(email)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("User with email: %s does not exist", email))
                );
    }

    @Override
    public boolean userWithEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean userWithPhoneNumberExists(String phoneNumber) {
        return userRepository.existsByDeliveryInfoPhoneNumber(phoneNumber);
    }
}
