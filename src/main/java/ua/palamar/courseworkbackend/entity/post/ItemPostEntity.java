package ua.palamar.courseworkbackend.entity.post;

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
@AllArgsConstructor
@NoArgsConstructor
public class ItemPostEntity extends Post{

    @OneToOne(
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            optional = false
    )
    private Dimensions dimensions;

}
