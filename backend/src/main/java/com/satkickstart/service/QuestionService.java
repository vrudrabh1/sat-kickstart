package com.satkickstart.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.satkickstart.dto.QuestionDto;
import com.satkickstart.model.Section;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * Fetches SAT practice questions from the OpenSAT / PineSAT API.
 * API docs: https://github.com/Anas099X/OpenSAT
 */
@Service
public class QuestionService {

    private final WebClient webClient;
    private final DifficultyService difficultyService;

    public QuestionService(@Value("${opensat.api.base-url}") String baseUrl,
                           WebClient.Builder webClientBuilder,
                           DifficultyService difficultyService) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.difficultyService = difficultyService;
    }

    /**
     * Fetches a batch of questions adapted to the user's current difficulty level.
     */
    public List<QuestionDto> getQuestionsForUser(String sessionId, Section section, int limit) {
        String sectionParam = section == Section.MATH ? "math" : "english";
        String domain = difficultyService.getDomainForUser(sessionId, section);
        return fetchFromApi(sectionParam, domain, limit);
    }

    /**
     * Fetches questions by explicit section without adaptive difficulty.
     */
    public List<QuestionDto> getQuestions(Section section, String domain, int limit) {
        String sectionParam = section == Section.MATH ? "math" : "english";
        return fetchFromApi(sectionParam, domain, limit);
    }

    private List<QuestionDto> fetchFromApi(String section, String domain, int limit) {
        List<OpenSatQuestion> raw = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/questions")
                        .queryParam("section", section)
                        .queryParamIfPresent("domain", java.util.Optional.ofNullable(domain))
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToFlux(OpenSatQuestion.class)
                .collectList()
                .block();

        if (raw == null) return List.of();
        return raw.stream().map(this::toDto).toList();
    }

    private QuestionDto toDto(OpenSatQuestion raw) {
        QuestionDto dto = new QuestionDto();
        dto.setId(raw.id);
        dto.setDomain(raw.domain);
        if (raw.question != null) {
            dto.setParagraph(raw.question.paragraph);
            dto.setQuestion(raw.question.question);
            dto.setChoices(raw.question.choices);
            dto.setCorrectAnswer(raw.question.correctAnswer);
            dto.setExplanation(raw.question.explanation);
        }
        return dto;
    }

    // Internal classes matching the OpenSAT API response shape
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class OpenSatQuestion {
        public String id;
        public String domain;
        public QuestionBody question;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class QuestionBody {
        public String paragraph;
        public String question;
        public Map<String, String> choices;
        @JsonProperty("correct_answer")
        public String correctAnswer;
        public String explanation;
    }
}
