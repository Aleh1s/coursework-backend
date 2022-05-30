package ua.palamar.courseworkbackend.entity.advertisement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ua.palamar.courseworkbackend.entity.image.Image;
import ua.palamar.courseworkbackend.entity.order.OrderEntity;
import ua.palamar.courseworkbackend.entity.user.UserAccount;

import javax.persistence.*;
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
    private AdvertisementCategory category;

    @Enumerated(STRING)
    @Column(nullable = false)
    private AdvertisementStatus status;

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
    private Set<OrderEntity> orders;

    @JsonIgnore
    @ManyToOne(
            fetch = LAZY
    )
    private UserAccount creator;

    @JsonIgnore
    @OneToOne(
            fetch = LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            optional = false
    )
    private Image image;

    @PrePersist
    public void setUp() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
        status = AdvertisementStatus.UNCHECKED;
    }

    public void addCreator(UserAccount creator) {
        this.creator = creator;
        creator.getAdvertisements().add(this);
    }

    public void removeCreator(UserAccount creator) {
        this.creator = null;
        creator.getAdvertisements().remove(this);
    }

    public void removeOrders(Set<OrderEntity> orderEntities) {
        orderEntities.forEach(order -> order.setProduct(null));
        this.orders.removeAll(orderEntities);
    }

    public Advertisement(
            String title,
            String description,
            AdvertisementCategory category,
            String city,
            Image image
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.city = city;
        this.image = image;
    }
}
