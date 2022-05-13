package ua.palamar.courseworkbackend.entity.advertisement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ItemAdvertisementEntity extends Advertisement {

    @JsonIgnore
    @OneToOne(
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            optional = false
    )
    private DimensionsEntity dimensions;


    @JsonIgnore
    @OneToMany(
            fetch = LAZY,
            orphanRemoval = true,
            cascade = ALL,
            mappedBy = "advertisement"
    )
    private Set<Order> orders;

    public ItemAdvertisementEntity(
            String id,
            String title,
            String description,
            Category category,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            UserEntity createdBy,
            Set<Order> orders,
            DimensionsEntity dimensions
    ) {
        super(
                id,
                title,
                description,
                category,
                createdAt,
                updatedAt,
                createdBy
        );
        this.orders = orders;
        this.dimensions = dimensions;
    }

    public void addOrder(Order order) {
        order.setAdvertisement(this);
        this.getOrders().add(order);
    }
    public void removeOrder(Order order) {
        order.setAdvertisement(null);
        this.getOrders().remove(order);
    }
}
