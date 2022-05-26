package ua.palamar.courseworkbackend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static ua.palamar.courseworkbackend.entity.order.DeliveryStatus.IN_PROCESS;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryEntity {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postOffice;

    @Enumerated(STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @PrePersist
    private void setUp() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        deliveryStatus = IN_PROCESS;
    }

    public DeliveryEntity(String city, String address, String postOffice) {
        this.city = city;
        this.address = address;
        this.postOffice = postOffice;
    }
}
