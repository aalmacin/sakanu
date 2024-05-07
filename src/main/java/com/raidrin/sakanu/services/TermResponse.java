package com.raidrin.sakanu.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raidrin.sakanu.entities.Term;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class TermResponse implements Serializable {
    private String searchTerm;
    private String domain;
    private String cloze;
    private String description;
    private String purpose;
    private String simpleExplanation;
    private List<Question> questions;
    private List<String> relatedTerms;
    private List<String> categories;

    @Getter
    @Setter
    public static class Question {
        private String question;
        private String answer;
    }

    public static TermResponse fromTerm(Term term) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        TermResponse termResponse = new TermResponse();
        termResponse.setSearchTerm(term.getTerm());
        termResponse.setDomain(term.getDomain());
        termResponse.setCloze(term.getCloze());
        termResponse.setDescription(term.getDescription());
        termResponse.setPurpose(term.getPurpose());
        termResponse.setSimpleExplanation(term.getSimpleExplanation());
        List<Question> questions = objectMapper.readValue(term.getQuestions(), new TypeReference<List<Question>>() {});
        termResponse.setQuestions(questions);

        List<String> categories = objectMapper.readValue(term.getCategories(), new TypeReference<List<String>>() {});
        termResponse.setCategories(categories);

        List<String> relatedTerms = objectMapper.readValue(term.getRelatedTerms(), new TypeReference<List<String>>() {});
        termResponse.setRelatedTerms(relatedTerms);
        return termResponse;
    }
}
