package ua.palamar.courseworkbackend.entity.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.user.UserInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ItemOrderEntity extends Order {

    @Column
    private String wishes;

    public ItemOrderEntity(
            String city,
            String address,
            String postNumber,
            String wishes
    ) {
        super(
                city,
                address,
                postNumber
        );
        this.wishes = wishes;
    }
}
