package ua.palamar.courseworkbackend.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;
import ua.palamar.courseworkbackend.entity.user.permissions.UserStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
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
@EqualsAndHashCode(of = "id")
public class UserEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate dob;

    @Enumerated(STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(STRING)
    @Column(nullable = false)
    private UserStatus status;

    @JsonIgnore
    @OneToOne(
            fetch = LAZY,
            optional = false,
            orphanRemoval = true,
            cascade = ALL
    )
    private UserInfo userInfo;

    @JsonIgnore
    @Setter(PRIVATE)
    @OneToMany(
            fetch = LAZY,
            orphanRemoval = true,
            cascade = ALL,
            mappedBy = "createdBy"
    )
    private Set<Advertisement> advertisements = new HashSet<>();

    @PrePersist
    public void setId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    public int getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }


}
