package ua.palamar.courseworkbackend.entity.feedback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackEntity {

    @Id
    private String id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void setUp() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
    }

    public FeedbackEntity(String email, String text) {
        this.email = email;
        this.text = text;
    }
}
