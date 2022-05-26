package ua.palamar.courseworkbackend.adapter.user;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.dto.response.UserResponseModel;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

@Component
public class UserDtoAdapterImpl implements UserDtoAdapter{

    @Override
    public UserResponseModel getModel(UserEntity user) {
        return new UserResponseModel(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber()
        );
    }
}
