package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.FeedbackCriteria;
import ua.palamar.courseworkbackend.dto.request.FeedbackModelRequest;
import ua.palamar.courseworkbackend.dto.response.FeedbackResponse;
import ua.palamar.courseworkbackend.dto.response.FeedbacksResponse;

public interface FeedbackService {

    FeedbackResponse create(FeedbackModelRequest request);

    FeedbacksResponse getAll(FeedbackCriteria criteria);
}
