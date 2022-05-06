package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.dto.RegistrationModel;
import ua.palamar.courseworkbackend.entity.user.UserInfo;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

public interface RegistrationServiceAdapter {

    UserEntity getUserEntity(RegistrationModel registrationModel, UserInfo userInfo);

    UserInfo getUserInfo(RegistrationModel registrationModel);
}
