package com.raidrin.sakanu.services;

import com.raidrin.sakanu.entities.Term;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
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
    static class Question {
        private String question;
        private String answer;
    }

    public static TermResponse fromTerm(Term term) {
        TermResponse termResponse = new TermResponse();
        termResponse.setSearchTerm(term.getTerm());
        termResponse.setDomain(term.getDomain());
        termResponse.setCloze(term.getCloze());
        termResponse.setDescription(term.getDescription());
        termResponse.setPurpose(term.getPurpose());
        termResponse.setSimpleExplanation(term.getSimpleExplanation());
        termResponse.setCategories(List.of(term.getCategories().split(",")));
        termResponse.setRelatedTerms(List.of(term.getCategories().split(",")));
        termResponse.setQuestions(Collections.emptyList());
        return termResponse;
    }
}
