package ua.palamar.courseworkbackend.adapter.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.RegistrationServiceAdapter;
import ua.palamar.courseworkbackend.dto.request.RegistrationRequestModel;
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
    public UserEntity getUserEntity(RegistrationRequestModel registrationRequestModel, UserInfo userInfo) {
        return new UserEntity(
                null,
                registrationRequestModel.email(),
                passwordEncoder.encode(registrationRequestModel.password()),
                registrationRequestModel.dob(),
                UserRole.USER,
                UserStatus.ACTIVE,
                userInfo,
                null
        );
    }

    @Override
    public UserInfo getUserInfo(RegistrationRequestModel registrationRequestModel) {
        return new UserInfo(
                null,
                registrationRequestModel.firstName(),
                registrationRequestModel.lastName(),
                registrationRequestModel.phoneNumber(),
                registrationRequestModel.city(),
                registrationRequestModel.address(),
                registrationRequestModel.postNumber()
        );
    }
}
