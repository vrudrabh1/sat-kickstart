package com.satkickstart.controller;

import com.satkickstart.dto.QuestionDto;
import com.satkickstart.model.Section;
import com.satkickstart.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * GET /api/v1/questions/adaptive?sessionId=xxx&section=MATH&limit=10
     * Returns questions adapted to the user's current difficulty level.
     */
    @GetMapping("/adaptive")
    public ResponseEntity<List<QuestionDto>> getAdaptiveQuestions(
            @RequestParam String sessionId,
            @RequestParam Section section,
            @RequestParam(defaultValue = "10") int limit) {
        List<QuestionDto> questions = questionService.getQuestionsForUser(sessionId, section, limit);
        return ResponseEntity.ok(questions);
    }

    /**
     * GET /api/v1/questions?section=MATH&domain=Algebra&limit=10
     * Returns questions by section and optional domain (no adaptive logic).
     */
    @GetMapping
    public ResponseEntity<List<QuestionDto>> getQuestions(
            @RequestParam Section section,
            @RequestParam(required = false) String domain,
            @RequestParam(defaultValue = "10") int limit) {
        List<QuestionDto> questions = questionService.getQuestions(section, domain, limit);
        return ResponseEntity.ok(questions);
    }
}
