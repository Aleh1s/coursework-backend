package ua.palamar.courseworkbackend.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @JsonIgnore
    @OneToOne(
            fetch = LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private DeliveryEntity deliveryEntity;

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private Advertisement product;

    @JsonIgnore
    @ManyToOne(
            fetch = LAZY,
            optional = false
    )
    private UserEntity sender;

    @JsonIgnore
    @ManyToOne(
            fetch = LAZY,
            optional = false
    )
    private UserEntity receiver;

    @Column
    private String wishes;

    @PrePersist
    public void setUp() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
        orderStatus = OrderStatus.UNCONFIRMED;
    }

    public OrderEntity(
            DeliveryEntity deliveryEntity,
            UserEntity sender,
            String wishes
    ) {
        this.wishes = wishes;
        this.sender = sender;
        this.deliveryEntity = deliveryEntity;
    }

    public void addReceiver(UserEntity receiver) {
        receiver.getOrderEntities().add(this);
        this.receiver = receiver;
    }

    public void removeReceiver(UserEntity receiver) {
        receiver.getOrderEntities().remove(this);
        this.receiver = null;
    }

    public void addAdvertisement(Advertisement product) {
        product.getOrderEntities().add(this);
        this.product = product;
    }

    public void removerAdvertisement(Advertisement product) {
        product.getOrderEntities().remove(this);
        this.product = null;
    }
}
