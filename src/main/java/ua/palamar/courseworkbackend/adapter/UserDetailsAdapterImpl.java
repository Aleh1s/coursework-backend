package ua.palamar.courseworkbackend.adapter;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.entity.user.UserDetails;
import ua.palamar.courseworkbackend.entity.user.UserAccount;

@Component
public class UserDetailsAdapterImpl implements UserDetailsAdapter{
    @Override
    public UserDetails getUserDetails(UserAccount userAccount) {
        return new UserDetails(
                userAccount.getEmail(),
                userAccount.getPassword(),
                userAccount.getRole()
        );
    }
}
