package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.dto.RegistrationModel;
import ua.palamar.courseworkbackend.entity.DeliveryInfoEntity;
import ua.palamar.courseworkbackend.entity.UserEntity;

public interface RegistrationServiceAdapter {

    UserEntity getUserEntity(RegistrationModel registrationModel);

    DeliveryInfoEntity getDeliveryInfoEntity(RegistrationModel registrationModel);
}
