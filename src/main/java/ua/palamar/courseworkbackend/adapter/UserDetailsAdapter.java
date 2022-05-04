package ua.palamar.courseworkbackend.adapter;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.entity.UserDetails;
import ua.palamar.courseworkbackend.entity.UserEntity;

public interface UserDetailsAdapter {
    UserDetails getUserDetails(String email);

}
