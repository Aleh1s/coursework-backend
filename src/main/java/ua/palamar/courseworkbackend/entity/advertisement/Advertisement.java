package ua.palamar.courseworkbackend.entity.advertisement;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = TABLE_PER_CLASS)
public abstract class Advertisement {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToOne(
            fetch = LAZY
    )
    private UserEntity createdBy;

    @PrePersist
    public void setId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    public void setUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }
}
