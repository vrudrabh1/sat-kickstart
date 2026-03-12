package com.satkickstart.controller;

import com.satkickstart.dto.AnswerRequest;
import com.satkickstart.dto.AnswerResponse;
import com.satkickstart.model.UserProgress;
import com.satkickstart.service.DifficultyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/practice")
public class PracticeController {

    private final DifficultyService difficultyService;

    public PracticeController(DifficultyService difficultyService) {
        this.difficultyService = difficultyService;
    }

    /**
     * POST /api/v1/practice/answer
     * Submit an answer, receive feedback and updated difficulty.
     */
    @PostMapping("/answer")
    public ResponseEntity<AnswerResponse> submitAnswer(@Valid @RequestBody AnswerRequest request) {
        boolean correct = request.getSelectedAnswer()
                .equalsIgnoreCase(request.getCorrectAnswer());

        UserProgress updated = difficultyService.recordAnswer(
                request.getSessionId(),
                request.getSection(),
                correct
        );

        AnswerResponse response = new AnswerResponse(
                correct,
                null, // explanation comes from the frontend (included in question object)
                updated.getDifficultyLevel(),
                updated.getAccuracy(),
                updated.getTotalAttempts()
        );

        return ResponseEntity.ok(response);
    }
}
