package ua.palamar.courseworkbackend.service.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.request.FeedbackModelRequest;
import ua.palamar.courseworkbackend.entity.feedback.FeedbackEntity;
import ua.palamar.courseworkbackend.repository.FeedbackRepository;
import ua.palamar.courseworkbackend.service.FeedbackService;

@Service
public class SimpleFeedbackService implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public SimpleFeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public ResponseEntity<?> create(FeedbackModelRequest request) {

        FeedbackEntity feedbackEntity = new FeedbackEntity(
                request.email(),
                request.text()
        );

        FeedbackEntity save = feedbackRepository.save(feedbackEntity);

        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getSortedPage(Integer limit, Integer page, String sortBy) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortBy));
        Page<FeedbackEntity> sortedPage = feedbackRepository.getAll(pageable);
        return new ResponseEntity<>(sortedPage, HttpStatus.ACCEPTED);
    }

}
