package ua.palamar.courseworkbackend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.post.Advertisement;
import ua.palamar.courseworkbackend.entity.user.DeliveryInfoEntity;

import javax.persistence.*;

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

    @Column(nullable = false)
    private String createdAt;

    @OneToOne(
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            optional = false
    )
    private Advertisement advertisement;

}
