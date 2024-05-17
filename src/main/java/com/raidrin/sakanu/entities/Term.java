package com.raidrin.sakanu.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.raidrin.sakanu.services.TermResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String term;

    @Column(columnDefinition = "TEXT")
    private String domain;
    @Column(columnDefinition = "TEXT")
    private String cloze;

    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String purpose;
    @Column(columnDefinition = "TEXT")
    private String simpleExplanation;

    @Convert(converter = QuestionListConverter.class)
    private List<Question> questions;
    @Convert(converter = StringListConverter.class)
    private List<String> relatedTerms;
    @Convert(converter = StringListConverter.class)
    private List<String> categories;
    private String user;

    public static Term fromTermResponse(TermResponse termResponse) throws JsonProcessingException {
        Term term = new Term();
        term.setTerm(termResponse.getSearchTerm());
        term.setDomain(termResponse.getDomain());
        term.setCloze(termResponse.getCloze());
        term.setDescription(termResponse.getDescription());
        term.setPurpose(termResponse.getPurpose());
        term.setSimpleExplanation(termResponse.getSimpleExplanation());
        term.setQuestions(termResponse.getQuestions()
                .stream().map(Term::convertToTermQuestion).toList());
        term.setCategories(termResponse.getCategories());
        term.setRelatedTerms(termResponse.getRelatedTerms());
        return term;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Question {
        private String question;
        private String answer;
    }

    private static Question convertToTermQuestion(TermResponse.Question question) {
        Question termQuestion = new Question();
        termQuestion.setQuestion(question.getQuestion());
        termQuestion.setAnswer(question.getAnswer());
        return termQuestion;
    }
}
