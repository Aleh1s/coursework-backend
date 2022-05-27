package ua.palamar.courseworkbackend.adapter.userDetails;

import ua.palamar.courseworkbackend.entity.user.UserDetails;
import ua.palamar.courseworkbackend.entity.user.UserAccount;

public interface UserDetailsAdapter {

    UserDetails getUserDetails(UserAccount userAccount);

}
