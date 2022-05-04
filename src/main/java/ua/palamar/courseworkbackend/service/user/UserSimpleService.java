package ua.palamar.courseworkbackend.service.user;

import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.entity.UserEntity;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.service.UserService;

@Service
public class UserSimpleService implements UserService {

    private final UserRepository userRepository;

    public UserSimpleService(UserRepository userRepository) {
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
}
