package ua.palamar.courseworkbackend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.user.UserInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrderEntity extends Order {

    @ManyToOne(
            fetch = LAZY,
            optional = false
    )
    private UserInfo buyerInfo;

    @Enumerated(STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

}
