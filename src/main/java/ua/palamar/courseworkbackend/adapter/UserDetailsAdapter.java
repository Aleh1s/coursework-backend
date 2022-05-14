package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.entity.user.UserDetails;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

public interface UserDetailsAdapter {

    UserDetails getUserDetails(UserEntity userEntity);

}
