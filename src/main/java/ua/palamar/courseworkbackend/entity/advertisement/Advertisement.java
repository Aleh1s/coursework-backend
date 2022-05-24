package ua.palamar.courseworkbackend.entity.advertisement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.entity.image.ImageEntity;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2048)
    private String description;

    @Column(nullable = false)
    private String city;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @Setter(PRIVATE)
    @OneToMany(
            fetch = LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            mappedBy = "product"
    )
    private Set<OrderEntity> orderEntities;

    @JsonIgnore
    @ManyToOne(
            fetch = LAZY
    )
    private UserEntity creator;

    @JsonIgnore
    @OneToOne(
            fetch = LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            optional = false
    )
    private ImageEntity image;

    @PrePersist
    public void setUp() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
    }

    public void addCreator(UserEntity creator) {
        this.creator = creator;
        creator.getAdvertisements().add(this);
    }

    public void removeCreator(UserEntity creator) {
        this.creator = null;
        creator.getAdvertisements().remove(this);
    }

    public Advertisement(
            String title,
            String description,
            Category category,
            String city,
            ImageEntity image
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.city = city;
        this.image = image;
    }
}
