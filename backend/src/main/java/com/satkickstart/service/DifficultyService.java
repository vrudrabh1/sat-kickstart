package com.satkickstart.service;

import com.satkickstart.model.DifficultyLevel;
import com.satkickstart.model.Section;
import com.satkickstart.model.UserProgress;
import com.satkickstart.repository.UserProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Implements the adaptive difficulty algorithm described in CLAUDE.md.
 *
 * Domain tiers per section:
 *   MATH:    EASY=Heart of Algebra, Problem Solving
 *            MEDIUM=Data Analysis, Geometry
 *            HARD=Passport to Advanced Math
 *   ENGLISH: EASY=Grammar Basics, Main Idea
 *            MEDIUM=Vocabulary in Context, Inference
 *            HARD=Rhetoric & Style, Textual Evidence
 *
 * Escalation rules:
 *   2+ consecutive correct   → escalate to HARD
 *   2+ consecutive incorrect → de-escalate to EASY
 */
@Service
public class DifficultyService {

    private static final int STREAK_THRESHOLD = 2;

    private static final Map<DifficultyLevel, List<String>> MATH_DOMAINS = Map.of(
            DifficultyLevel.EASY,   List.of("Heart of Algebra", "Problem Solving and Data Analysis"),
            DifficultyLevel.MEDIUM, List.of("Data Analysis", "Geometry and Trigonometry"),
            DifficultyLevel.HARD,   List.of("Passport to Advanced Math")
    );

    private static final Map<DifficultyLevel, List<String>> ENGLISH_DOMAINS = Map.of(
            DifficultyLevel.EASY,   List.of("Standard English Conventions", "Information and Ideas"),
            DifficultyLevel.MEDIUM, List.of("Craft and Structure", "Expression of Ideas"),
            DifficultyLevel.HARD,   List.of("Craft and Structure")
    );

    private final UserProgressRepository progressRepository;

    public DifficultyService(UserProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    /**
     * Returns the best matching domain for the user's current difficulty level.
     */
    public String getDomainForUser(String sessionId, Section section) {
        UserProgress progress = getOrCreate(sessionId, section);
        List<String> domains = domainsFor(section, progress.getDifficultyLevel());
        return domains.get(0);
    }

    /**
     * Records an answer, updates streaks, and adjusts difficulty level if needed.
     * Returns the updated UserProgress.
     */
    @Transactional
    public UserProgress recordAnswer(String sessionId, Section section, boolean correct) {
        UserProgress progress = getOrCreate(sessionId, section);

        progress.setTotalAttempts(progress.getTotalAttempts() + 1);

        if (correct) {
            progress.setCorrectAttempts(progress.getCorrectAttempts() + 1);
            progress.setConsecutiveCorrect(progress.getConsecutiveCorrect() + 1);
            progress.setConsecutiveIncorrect(0);

            if (progress.getConsecutiveCorrect() >= STREAK_THRESHOLD) {
                escalate(progress);
                progress.setConsecutiveCorrect(0);
            }
        } else {
            progress.setConsecutiveIncorrect(progress.getConsecutiveIncorrect() + 1);
            progress.setConsecutiveCorrect(0);

            if (progress.getConsecutiveIncorrect() >= STREAK_THRESHOLD) {
                deescalate(progress);
                progress.setConsecutiveIncorrect(0);
            }
        }

        return progressRepository.save(progress);
    }

    private void escalate(UserProgress progress) {
        if (progress.getDifficultyLevel() == DifficultyLevel.EASY) {
            progress.setDifficultyLevel(DifficultyLevel.MEDIUM);
        } else if (progress.getDifficultyLevel() == DifficultyLevel.MEDIUM) {
            progress.setDifficultyLevel(DifficultyLevel.HARD);
        }
    }

    private void deescalate(UserProgress progress) {
        if (progress.getDifficultyLevel() == DifficultyLevel.HARD) {
            progress.setDifficultyLevel(DifficultyLevel.MEDIUM);
        } else if (progress.getDifficultyLevel() == DifficultyLevel.MEDIUM) {
            progress.setDifficultyLevel(DifficultyLevel.EASY);
        }
    }

    private List<String> domainsFor(Section section, DifficultyLevel level) {
        return section == Section.MATH
                ? MATH_DOMAINS.getOrDefault(level, MATH_DOMAINS.get(DifficultyLevel.MEDIUM))
                : ENGLISH_DOMAINS.getOrDefault(level, ENGLISH_DOMAINS.get(DifficultyLevel.MEDIUM));
    }

    private UserProgress getOrCreate(String sessionId, Section section) {
        return progressRepository
                .findBySessionIdAndSection(sessionId, section)
                .orElseGet(() -> progressRepository.save(new UserProgress(sessionId, section)));
    }
}
