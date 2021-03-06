package ua.palamar.courseworkbackend.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.user.UserAccount;

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
            cascade = CascadeType.ALL,
            optional = true
    )
    private Delivery delivery;

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
    private UserAccount sender;

    @JsonIgnore
    @ManyToOne(
            fetch = LAZY,
            optional = false
    )
    private UserAccount receiver;

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
            Delivery delivery,
            UserAccount sender,
            String wishes
    ) {
        this.wishes = wishes;
        this.sender = sender;
        this.delivery = delivery;
    }

    public void addReceiver(UserAccount receiver) {
        receiver.getOrderEntityEntities().add(this);
        this.receiver = receiver;
    }


    public void addAdvertisement(Advertisement product) {
        product.getOrders().add(this);
        this.product = product;
    }

}
