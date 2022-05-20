package ua.palamar.courseworkbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.palamar.courseworkbackend.entity.feedback.FeedbackEntity;

import java.util.List;

@Repository
public interface FeedbackRepository extends PagingAndSortingRepository<FeedbackEntity, String> {

}
