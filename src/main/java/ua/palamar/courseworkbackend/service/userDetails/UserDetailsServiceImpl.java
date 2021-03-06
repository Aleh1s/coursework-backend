package ua.palamar.courseworkbackend.service.userDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.adapter.userDetails.UserDetailsAdapter;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsAdapter userDetailsAdapter;
    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(
            UserDetailsAdapter userDetailsAdapter,
            UserService userService
    ) {
        this.userDetailsAdapter = userDetailsAdapter;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount user = userService.getUserEntityByEmail(email);
        return userDetailsAdapter.getUserDetails(user);
    }
}
