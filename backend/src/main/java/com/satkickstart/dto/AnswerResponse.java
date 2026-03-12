package com.satkickstart.dto;

import com.satkickstart.model.DifficultyLevel;

public class AnswerResponse {

    private boolean correct;
    private String explanation;
    private DifficultyLevel newDifficultyLevel;
    private double accuracy;
    private int totalAttempts;

    public AnswerResponse() {}

    public AnswerResponse(boolean correct, String explanation,
                          DifficultyLevel newDifficultyLevel,
                          double accuracy, int totalAttempts) {
        this.correct = correct;
        this.explanation = explanation;
        this.newDifficultyLevel = newDifficultyLevel;
        this.accuracy = accuracy;
        this.totalAttempts = totalAttempts;
    }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public DifficultyLevel getNewDifficultyLevel() { return newDifficultyLevel; }
    public void setNewDifficultyLevel(DifficultyLevel newDifficultyLevel) {
        this.newDifficultyLevel = newDifficultyLevel;
    }

    public double getAccuracy() { return accuracy; }
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }

    public int getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(int totalAttempts) { this.totalAttempts = totalAttempts; }
}
