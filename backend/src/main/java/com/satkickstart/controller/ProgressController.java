package com.satkickstart.controller;

import com.satkickstart.dto.ProgressDto;
import com.satkickstart.model.UserProgress;
import com.satkickstart.repository.UserProgressRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private final UserProgressRepository progressRepository;

    public ProgressController(UserProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    /**
     * GET /api/v1/progress?sessionId=xxx
     * Returns progress summary for all sections for the given session.
     */
    @GetMapping
    public ResponseEntity<List<ProgressDto>> getProgress(@RequestParam String sessionId) {
        List<UserProgress> all = progressRepository.findBySessionId(sessionId);
        List<ProgressDto> result = all.stream().map(this::toDto).toList();
        return ResponseEntity.ok(result);
    }

    private ProgressDto toDto(UserProgress p) {
        ProgressDto dto = new ProgressDto();
        dto.setSection(p.getSection());
        dto.setDifficultyLevel(p.getDifficultyLevel());
        dto.setTotalAttempts(p.getTotalAttempts());
        dto.setCorrectAttempts(p.getCorrectAttempts());
        dto.setAccuracy(p.getAccuracy());
        return dto;
    }
}
