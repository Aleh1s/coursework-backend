package ua.palamar.courseworkbackend.adapter.user;

import ua.palamar.courseworkbackend.dto.response.UserResponse;
import ua.palamar.courseworkbackend.entity.user.UserAccount;

public interface UserDtoAdapter {

    UserResponse getModel(UserAccount user);

}
