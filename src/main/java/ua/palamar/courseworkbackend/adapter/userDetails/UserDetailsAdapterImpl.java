package ua.palamar.courseworkbackend.adapter.userDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.UserDetailsAdapter;
import ua.palamar.courseworkbackend.entity.UserDetails;
import ua.palamar.courseworkbackend.entity.UserEntity;
import ua.palamar.courseworkbackend.service.user.SimpleUserService;

@Component
public class UserDetailsAdapterImpl implements UserDetailsAdapter {

    private final SimpleUserService userService;

    @Autowired
    public UserDetailsAdapterImpl(SimpleUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails getUserDetails(String email) {
        UserEntity userEntity = userService.getUserEntityByEmail(email);
        return toUserDetails(userEntity);
    }

    private UserDetails toUserDetails(UserEntity userEntity) {
        return new UserDetails(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getStatus(),
                userEntity.getRole()
        );
    }
}
