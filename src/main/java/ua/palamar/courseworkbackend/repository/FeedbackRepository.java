package ua.palamar.courseworkbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.dto.response.FeedbackResponse;
import ua.palamar.courseworkbackend.entity.feedback.FeedbackEntity;

import java.util.List;
import java.util.Set;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, String> {

    Set<FeedbackEntity> getAll(Pageable pageable);

}
