package ua.palamar.courseworkbackend.entity;

import lombok.*;
import ua.palamar.courseworkbackend.entity.permissions.UserRole;
import ua.palamar.courseworkbackend.entity.permissions.UserStatus;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(STRING)
    @Column(nullable = false)
    private UserStatus status;

    @PrePersist
    public void setId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

}
