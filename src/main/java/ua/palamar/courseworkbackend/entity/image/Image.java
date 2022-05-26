package ua.palamar.courseworkbackend.entity.image;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String originalFileName;
    @Column(nullable = false)
    private Long size;
    @Column(nullable = false)
    private String contentType;
    @Lob
    @Column(nullable = false)
    private byte[] bytes;
    @Column
    private LocalDateTime uploadedAt;
    @PrePersist
    private void setUp() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        uploadedAt = LocalDateTime.now();
    }

    public Image(String name, String originalFileName, Long size, String contentType, byte[] bytes) {
        this.name = name;
        this.originalFileName = originalFileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}