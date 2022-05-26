package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> getUserEntityByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select u from UserEntity u join fetch u.advertisements where u.email = :email")
    Optional<UserEntity> findUserEntityByEmailJoinFetchAdvertisements(String email);

    @Query("select u from UserEntity u join fetch u.image where u.email = :email")
    Optional<UserEntity> findUserEntityByEmailJoinFetchImage(String email);
}
