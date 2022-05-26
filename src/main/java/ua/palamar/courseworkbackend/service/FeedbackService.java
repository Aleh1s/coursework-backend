package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import ua.palamar.courseworkbackend.dto.request.FeedbackModelRequest;

public interface FeedbackService {

    ResponseEntity<?> create(FeedbackModelRequest request);

    ResponseEntity<?> getSortedPage(Integer limit, Integer page, String sortBy);
}
