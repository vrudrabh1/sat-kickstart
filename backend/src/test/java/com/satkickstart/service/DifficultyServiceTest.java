package com.satkickstart.service;

import com.satkickstart.model.DifficultyLevel;
import com.satkickstart.model.Section;
import com.satkickstart.model.UserProgress;
import com.satkickstart.repository.UserProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DifficultyServiceTest {

    @Mock
    private UserProgressRepository progressRepository;

    @InjectMocks
    private DifficultyService difficultyService;

    private UserProgress progress;

    @BeforeEach
    void setUp() {
        progress = new UserProgress("session-1", Section.MATH);
        when(progressRepository.findBySessionIdAndSection(any(), any()))
                .thenReturn(Optional.of(progress));
        when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void newUserStartsAtMedium() {
        assertThat(progress.getDifficultyLevel()).isEqualTo(DifficultyLevel.MEDIUM);
    }

    @Test
    void twoConsecutiveCorrectEscalatesToHard() {
        difficultyService.recordAnswer("session-1", Section.MATH, true);
        difficultyService.recordAnswer("session-1", Section.MATH, true);

        assertThat(progress.getDifficultyLevel()).isEqualTo(DifficultyLevel.HARD);
    }

    @Test
    void twoConsecutiveIncorrectDeescalatesToEasy() {
        difficultyService.recordAnswer("session-1", Section.MATH, false);
        difficultyService.recordAnswer("session-1", Section.MATH, false);

        assertThat(progress.getDifficultyLevel()).isEqualTo(DifficultyLevel.EASY);
    }

    @Test
    void incorrectAnswerResetsCorrectStreak() {
        difficultyService.recordAnswer("session-1", Section.MATH, true);
        difficultyService.recordAnswer("session-1", Section.MATH, false); // resets streak
        difficultyService.recordAnswer("session-1", Section.MATH, true); // streak = 1 only

        // Should NOT have escalated because streak was broken
        assertThat(progress.getDifficultyLevel()).isEqualTo(DifficultyLevel.MEDIUM);
    }

    @Test
    void accuracyCalculatedCorrectly() {
        difficultyService.recordAnswer("session-1", Section.MATH, true);
        difficultyService.recordAnswer("session-1", Section.MATH, false);
        difficultyService.recordAnswer("session-1", Section.MATH, true);

        assertThat(progress.getTotalAttempts()).isEqualTo(3);
        assertThat(progress.getCorrectAttempts()).isEqualTo(2);
        assertThat(progress.getAccuracy()).isCloseTo(66.66, org.assertj.core.data.Offset.offset(0.1));
    }
}
