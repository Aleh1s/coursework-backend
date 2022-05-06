package ua.palamar.courseworkbackend.entity.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.TABLE_PER_CLASS;
import static ua.palamar.courseworkbackend.entity.post.AdvertisementStatus.UNCONFIRMED;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = TABLE_PER_CLASS)
public abstract class Advertisement {

    @Id
    private String id;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(STRING)
    @Column(nullable = false)
    private AdvertisementStatus status;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime removedAt;

    @ManyToOne(
            fetch = LAZY
    )
    private UserEntity createdBy;

    @OneToMany(
            fetch = LAZY,
            orphanRemoval = true,
            cascade = ALL,
            mappedBy = "advertisement"
    )
    private Set<Order> orders;

    @PrePersist
    public void setCreatedAt() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
        status = UNCONFIRMED;
    }

    @PreUpdate
    public void setUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }

    @PreRemove
    public void setRemovedAt() {
        removedAt = LocalDateTime.now();
    }
}
