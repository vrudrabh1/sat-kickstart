package com.satkickstart.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_progress",
       uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "section"}))
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Section section;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;

    @Column(nullable = false)
    private int consecutiveCorrect = 0;

    @Column(nullable = false)
    private int consecutiveIncorrect = 0;

    @Column(nullable = false)
    private int totalAttempts = 0;

    @Column(nullable = false)
    private int correctAttempts = 0;

    public UserProgress() {}

    public UserProgress(String sessionId, Section section) {
        this.sessionId = sessionId;
        this.section = section;
    }

    public Long getId() { return id; }

    public String getSessionId() { return sessionId; }

    public Section getSection() { return section; }

    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getConsecutiveCorrect() { return consecutiveCorrect; }
    public void setConsecutiveCorrect(int consecutiveCorrect) {
        this.consecutiveCorrect = consecutiveCorrect;
    }

    public int getConsecutiveIncorrect() { return consecutiveIncorrect; }
    public void setConsecutiveIncorrect(int consecutiveIncorrect) {
        this.consecutiveIncorrect = consecutiveIncorrect;
    }

    public int getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(int totalAttempts) { this.totalAttempts = totalAttempts; }

    public int getCorrectAttempts() { return correctAttempts; }
    public void setCorrectAttempts(int correctAttempts) {
        this.correctAttempts = correctAttempts;
    }

    public double getAccuracy() {
        if (totalAttempts == 0) return 0.0;
        return (double) correctAttempts / totalAttempts * 100;
    }
}
