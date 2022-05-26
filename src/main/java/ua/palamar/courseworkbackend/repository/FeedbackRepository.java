package ua.palamar.courseworkbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.entity.feedback.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {

}
