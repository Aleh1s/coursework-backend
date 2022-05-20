package ua.palamar.courseworkbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.palamar.courseworkbackend.entity.feedback.FeedbackEntity;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, String> {

    Page<FeedbackEntity> getAll(Pageable pageable);

}
