package ua.palamar.courseworkbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.palamar.courseworkbackend.dto.request.FeedbackModelRequest;
import ua.palamar.courseworkbackend.service.FeedbackService;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody FeedbackModelRequest request
    ) {
        return feedbackService.create(request);
    }

    @GetMapping
    public ResponseEntity<?> getSortedPage(
            @RequestParam("_limit") Integer limit,
            @RequestParam("_page") Integer page,
            @RequestParam("_sortBy") String sortBy
    ) {
        return feedbackService.getSortedPage(limit, page, sortBy);
    }

}
