package ua.palamar.courseworkbackend.entity.advertisement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DimensionsEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private int length;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private int width;

    @PrePersist
    public void setUp() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

}
