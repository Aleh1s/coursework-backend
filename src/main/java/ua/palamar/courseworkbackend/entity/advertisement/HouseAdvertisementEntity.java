package ua.palamar.courseworkbackend.entity.advertisement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseAdvertisementEntity extends Advertisement {

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private DimensionsEntity dimensions;

}
