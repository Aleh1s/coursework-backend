package ua.palamar.courseworkbackend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.entity.feedback.Feedback;

import java.util.Set;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {

    Set<Feedback> getAll(Pageable pageable);

}
