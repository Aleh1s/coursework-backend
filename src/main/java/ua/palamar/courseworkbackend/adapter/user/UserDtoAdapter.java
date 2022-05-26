package ua.palamar.courseworkbackend.adapter.user;

import ua.palamar.courseworkbackend.dto.response.UserResponseModel;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

public interface UserDtoAdapter {

    UserResponseModel getModel(UserEntity user);

}
