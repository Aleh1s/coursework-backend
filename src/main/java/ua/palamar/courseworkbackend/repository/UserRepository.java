package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.entity.user.UserStatus;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, String> {

    Optional<UserAccount> getUserEntityByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select u from UserAccount u join fetch u.image where u.email = :email")
    Optional<UserAccount> findUserEntityByEmailJoinFetchImage(String email);

    @Query("update UserAccount u set u.status = :status where u.email = :email")
    @Modifying
    @Transactional
    void updateUserStatusById(String email, UserStatus status);
}
