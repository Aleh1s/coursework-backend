package ua.palamar.courseworkbackend.service;

import ua.palamar.courseworkbackend.entity.UserEntity;

public interface UserService {

    UserEntity getUserEntityById(String id);
    UserEntity getUserEntityByEmail(String email);

}
