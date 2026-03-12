package com.satkickstart.dto;

import com.satkickstart.model.Section;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AnswerRequest {

    @NotBlank
    private String sessionId;

    @NotNull
    private Section section;

    @NotBlank
    private String questionId;

    @NotBlank
    private String selectedAnswer;

    @NotBlank
    private String correctAnswer;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }

    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}
