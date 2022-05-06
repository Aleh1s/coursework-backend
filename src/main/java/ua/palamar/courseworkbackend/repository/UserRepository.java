package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> getUserEntityByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByUserInfoPhoneNumber(String phoneNumber);

}
