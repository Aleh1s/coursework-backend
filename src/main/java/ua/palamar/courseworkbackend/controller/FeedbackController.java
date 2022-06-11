package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.criteria.FeedbackCriteria;
import ua.palamar.courseworkbackend.dto.request.FeedbackRequest;
import ua.palamar.courseworkbackend.dto.response.FeedbackResponse;
import ua.palamar.courseworkbackend.dto.response.FeedbacksResponse;
import ua.palamar.courseworkbackend.service.FeedbackService;

@RestController
@RequestMapping("/api/v1/feedback")
@CrossOrigin("http://localhost:3000")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> create(
            @RequestBody FeedbackRequest request
    ) {
        return new ResponseEntity<>(
                feedbackService.create(request), HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<FeedbacksResponse> getSortedPage(
            @RequestParam("_limit") Integer limit,
            @RequestParam("_page") Integer page,
            @RequestParam("_sortBy") String sortBy
    ) {
        return new ResponseEntity<>(feedbackService.getAll(new FeedbackCriteria(limit, page, sortBy)), HttpStatus.OK);
    }
}
