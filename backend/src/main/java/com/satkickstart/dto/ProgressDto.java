package com.satkickstart.dto;

import com.satkickstart.model.DifficultyLevel;
import com.satkickstart.model.Section;

public class ProgressDto {

    private Section section;
    private DifficultyLevel difficultyLevel;
    private int totalAttempts;
    private int correctAttempts;
    private double accuracy;

    public ProgressDto() {}

    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }

    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(int totalAttempts) { this.totalAttempts = totalAttempts; }

    public int getCorrectAttempts() { return correctAttempts; }
    public void setCorrectAttempts(int correctAttempts) {
        this.correctAttempts = correctAttempts;
    }

    public double getAccuracy() { return accuracy; }
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }
}
