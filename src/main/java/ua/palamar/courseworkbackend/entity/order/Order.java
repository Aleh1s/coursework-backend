package ua.palamar.courseworkbackend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private LocalDateTime createdAt;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private Advertisement advertisement;

    @PrePersist
    public void setUp() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
    }

}
