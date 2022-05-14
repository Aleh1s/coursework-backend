package ua.palamar.courseworkbackend.adapter;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.entity.user.UserDetails;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

@Component
public class UserDetailsAdapterImpl implements UserDetailsAdapter{
    @Override
    public UserDetails getUserDetails(UserEntity userEntity) {
        return new UserDetails(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRole()
        );
    }
}
