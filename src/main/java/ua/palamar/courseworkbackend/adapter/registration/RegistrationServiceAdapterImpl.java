package ua.palamar.courseworkbackend.adapter.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.RegistrationServiceAdapter;
import ua.palamar.courseworkbackend.dto.RegistrationModel;
import ua.palamar.courseworkbackend.entity.user.UserInfo;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;
import ua.palamar.courseworkbackend.entity.user.permissions.UserStatus;

@Component
public class RegistrationServiceAdapterImpl implements RegistrationServiceAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationServiceAdapterImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity getUserEntity(RegistrationModel registrationModel, UserInfo userInfo) {
        return new UserEntity(
                null,
                registrationModel.email(),
                passwordEncoder.encode(registrationModel.password()),
                registrationModel.dob(),
                UserRole.USER,
                UserStatus.ACTIVE,
                userInfo,
                null
        );
    }

    @Override
    public UserInfo getUserInfo(RegistrationModel registrationModel) {
        return new UserInfo(
                null,
                registrationModel.firstName(),
                registrationModel.lastName(),
                registrationModel.phoneNumber(),
                registrationModel.city(),
                registrationModel.address(),
                registrationModel.postNumber()
        );
    }
}
