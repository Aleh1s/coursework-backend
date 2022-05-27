package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.entity.image.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {}
