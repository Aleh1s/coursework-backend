package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.dto.response.UserResponseModel;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

public interface AuthenticationServiceAdapter {

    UserResponseModel getUserModel(UserEntity user);

}
