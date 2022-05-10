package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.dto.request.RegistrationRequestModel;
import ua.palamar.courseworkbackend.entity.user.UserInfo;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

public interface RegistrationServiceAdapter {

    UserEntity getUserEntity(RegistrationRequestModel registrationRequestModel, UserInfo userInfo);

    UserInfo getUserInfo(RegistrationRequestModel registrationRequestModel);
}
