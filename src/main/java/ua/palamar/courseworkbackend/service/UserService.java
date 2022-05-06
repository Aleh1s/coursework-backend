package ua.palamar.courseworkbackend.service;

import ua.palamar.courseworkbackend.entity.user.UserEntity;

public interface UserService {

    UserEntity getUserEntityById(String id);
    UserEntity getUserEntityByEmail(String email);

}
