package ua.palamar.courseworkbackend.service;

import ua.palamar.courseworkbackend.dto.criteria.FeedbackCriteria;
import ua.palamar.courseworkbackend.dto.request.FeedbackRequest;
import ua.palamar.courseworkbackend.dto.response.FeedbackResponse;
import ua.palamar.courseworkbackend.dto.response.FeedbacksResponse;

public interface FeedbackService {

    FeedbackResponse create(FeedbackRequest request);

    FeedbacksResponse getAll(FeedbackCriteria criteria);
}
