package ua.palamar.courseworkbackend.adapter.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.RegistrationServiceAdapter;
import ua.palamar.courseworkbackend.dto.RegistrationModel;
import ua.palamar.courseworkbackend.entity.user.DeliveryInfoEntity;
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
    public UserEntity getUserEntity(RegistrationModel registrationModel) {
        return new UserEntity(
                null,
                registrationModel.email(),
                passwordEncoder.encode(registrationModel.password()),
                registrationModel.firstName(),
                registrationModel.lastName(),
                registrationModel.dob(),
                UserRole.USER,
                UserStatus.ACTIVE,
                null
        );
    }

    @Override
    public DeliveryInfoEntity getDeliveryInfoEntity(RegistrationModel registrationModel) {
        return new DeliveryInfoEntity(
                null,
                registrationModel.phoneNumber(),
                registrationModel.city(),
                registrationModel.address(),
                registrationModel.postNumber(),
                null
        );
    }
}
