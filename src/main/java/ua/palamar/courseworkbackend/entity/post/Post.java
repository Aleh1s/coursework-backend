package ua.palamar.courseworkbackend.entity.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class Post {

    @Id
    private String id;

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

    @ManyToOne(fetch = LAZY)
    private UserEntity creator;

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
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
