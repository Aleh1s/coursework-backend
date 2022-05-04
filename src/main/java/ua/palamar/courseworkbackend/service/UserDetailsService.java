package ua.palamar.courseworkbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.adapter.UserDetailsAdapter;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserDetailsAdapter userDetailsAdapter;

    @Autowired
    public UserDetailsService(UserDetailsAdapter userDetailsAdapter) {
        this.userDetailsAdapter = userDetailsAdapter;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDetailsAdapter.getUserDetails(email);
    }
}
