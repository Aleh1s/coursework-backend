package ua.palamar.courseworkbackend.entity;

import lombok.*;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class DeliveryInfoEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postNumber;

    @OneToOne(fetch = LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private UserEntity user;

    @PrePersist
    public void setId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
