package com.satkickstart.dto;

import java.util.Map;

public class QuestionDto {

    private String id;
    private String domain;
    private String paragraph;
    private String question;
    private Map<String, String> choices;
    private String correctAnswer;
    private String explanation;

    public QuestionDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public String getParagraph() { return paragraph; }
    public void setParagraph(String paragraph) { this.paragraph = paragraph; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public Map<String, String> getChoices() { return choices; }
    public void setChoices(Map<String, String> choices) { this.choices = choices; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
