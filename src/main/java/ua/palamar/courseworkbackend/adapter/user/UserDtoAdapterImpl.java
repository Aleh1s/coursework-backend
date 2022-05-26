package ua.palamar.courseworkbackend.adapter.user;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.dto.response.UserResponse;
import ua.palamar.courseworkbackend.entity.user.UserAccount;

@Component
public class UserDtoAdapterImpl implements UserDtoAdapter{

    @Override
    public UserResponse getModel(UserAccount user) {
        return new UserResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber()
        );
    }
}
