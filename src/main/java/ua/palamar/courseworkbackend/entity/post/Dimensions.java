package ua.palamar.courseworkbackend.entity.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dimensions {

    @Id
    private String id;

    @Column(nullable = false)
    private int length;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private int width;

}
