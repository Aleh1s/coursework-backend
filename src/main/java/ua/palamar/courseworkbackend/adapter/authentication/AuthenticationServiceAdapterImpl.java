package ua.palamar.courseworkbackend.adapter.authentication;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.AuthenticationServiceAdapter;
import ua.palamar.courseworkbackend.dto.response.UserResponseModel;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

@Component
public class AuthenticationServiceAdapterImpl implements AuthenticationServiceAdapter {

    @Override
    public UserResponseModel getUserModel(UserEntity user) {
        return new UserResponseModel(
                user.getEmail(),
                user.getUserInfo().getFirstName(),
                user.getUserInfo().getLastName(),
                user.getUserInfo().getCity(),
                user.getUserInfo().getPhoneNumber(),
                user.getAge()
        );
    }
}
