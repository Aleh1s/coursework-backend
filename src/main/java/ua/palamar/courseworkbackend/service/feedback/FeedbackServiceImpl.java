package ua.palamar.courseworkbackend.service.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.criteria.FeedbackCriteria;
import ua.palamar.courseworkbackend.dto.request.FeedbackRequest;
import ua.palamar.courseworkbackend.dto.response.FeedbackResponse;
import ua.palamar.courseworkbackend.dto.response.FeedbacksResponse;
import ua.palamar.courseworkbackend.entity.feedback.Feedback;
import ua.palamar.courseworkbackend.repository.FeedbackRepository;
import ua.palamar.courseworkbackend.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public FeedbackResponse create(FeedbackRequest request) {
        Feedback feedbackEntity = new Feedback(
                request.email(),
                request.text()
        );

        Feedback feedback = feedbackRepository.save(feedbackEntity);

        return new FeedbackResponse(
                feedback.getId(),
                feedback.getEmail(),
                feedback.getText()
        );
    }

    @Override
    public FeedbacksResponse getAll(FeedbackCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.page(), criteria.limit(), Sort.by(criteria.sortBy()).descending());

        Page<Feedback> feedbacks = feedbackRepository.findAll(pageable);
        Long count = feedbackRepository.count();

        return new FeedbacksResponse(
                feedbacks,
                count
        );
    }
}
