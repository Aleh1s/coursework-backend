package ua.palamar.courseworkbackend.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.entity.UserDetails;
import ua.palamar.courseworkbackend.entity.UserEntity;
import ua.palamar.courseworkbackend.service.user.UserSimpleService;

@Component
public class UserDetailsAdapterImpl implements UserDetailsAdapter {

    private final UserSimpleService userService;

    @Autowired
    public UserDetailsAdapterImpl(UserSimpleService userService) {
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
