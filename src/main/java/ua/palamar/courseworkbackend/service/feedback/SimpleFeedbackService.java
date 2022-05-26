package ua.palamar.courseworkbackend.service.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.FeedbackCriteria;
import ua.palamar.courseworkbackend.dto.request.FeedbackModelRequest;
import ua.palamar.courseworkbackend.dto.response.FeedbackResponse;
import ua.palamar.courseworkbackend.dto.response.FeedbacksResponse;
import ua.palamar.courseworkbackend.entity.feedback.FeedbackEntity;
import ua.palamar.courseworkbackend.repository.FeedbackRepository;
import ua.palamar.courseworkbackend.service.FeedbackService;

import java.util.Set;

@Service
public class SimpleFeedbackService implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public SimpleFeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public FeedbackResponse create(FeedbackModelRequest request) {
        FeedbackEntity feedbackEntity = new FeedbackEntity(
                request.email(),
                request.text()
        );

        FeedbackEntity feedback = feedbackRepository.save(feedbackEntity);

        return new FeedbackResponse(
                feedback.getId(),
                feedback.getEmail(),
                feedback.getText()
        );
    }

    @Override
    public FeedbacksResponse getAll(FeedbackCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.page(), criteria.limit(), Sort.by(criteria.sortBy()).descending());

        Set<FeedbackEntity> feedbacks = feedbackRepository.getAll(pageable);
        Long count = feedbackRepository.count();

        return new FeedbacksResponse(
                feedbacks,
                count
        );
    }
}
