package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.entity.image.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String> {

}
