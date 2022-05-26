package ua.palamar.courseworkbackend.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.image.Image;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;
import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {

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

    @Column(nullable = false)
    private String phoneNumber;

    @JsonIgnore
    @OneToOne(
            fetch = LAZY,
            cascade = ALL,
            optional = true,
            orphanRemoval = true
    )
    private Image image;

    @JsonIgnore
    @Setter(PRIVATE)
    @OneToMany(
            fetch = LAZY,
            orphanRemoval = true,
            cascade = ALL,
            mappedBy = "creator"
    )
    private Set<Advertisement> advertisements = new HashSet<>();

    @JsonIgnore
    @Setter(PRIVATE)
    @OneToMany(
            fetch = LAZY,
            orphanRemoval = true,
            cascade = ALL,
            mappedBy = "receiver"
    )
    private Set<OrderEntity> orderEntityEntities = new HashSet<>();

    @PrePersist
    public void setId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    public UserAccount(
            String email,
            String password,
            String firstName,
            String lastName,
            UserRole role,
            String phoneNumber
    ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }
}
