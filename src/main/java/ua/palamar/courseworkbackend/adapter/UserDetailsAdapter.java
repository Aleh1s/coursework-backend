package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.entity.user.UserDetails;

public interface UserDetailsAdapter {
    UserDetails getUserDetails(String email);

}
