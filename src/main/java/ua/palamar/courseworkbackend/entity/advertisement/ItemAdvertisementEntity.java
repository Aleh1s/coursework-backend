package ua.palamar.courseworkbackend.entity.advertisement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.order.Order;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
//@AllArgsConstructor
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


    public ItemAdvertisementEntity(
            String id,
            String title,
            String description,
            Category category,
            AdvertisementStatus status,
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
                status,
                createdAt,
                updatedAt,
                createdBy,
                orders
        );
        this.dimensions = dimensions;
    }
}
