package ua.palamar.courseworkbackend.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.user.UserInfo;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = TABLE_PER_CLASS)
public abstract class Order {

    @Id
    private String id;

    @Column
    private String city;

    @Column
    private String address;

    @Column
    private String postNumber;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private Advertisement advertisement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @JsonIgnore
    @ManyToOne(
            fetch = LAZY,
            optional = false
    )
    private UserInfo orderedBy;

    @PrePersist
    public void setUp() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
        deliveryStatus = DeliveryStatus.IN_PROCESS;
        orderStatus = OrderStatus.UNCONFIRMED;
    }

    public Order(String city, String address, String postNumber) {
        this.city = city;
        this.address = address;
        this.postNumber = postNumber;
    }
}
