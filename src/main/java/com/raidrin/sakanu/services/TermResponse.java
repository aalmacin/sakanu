package com.raidrin.sakanu.services;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class TermResponse implements Serializable {
    private String searchTerm;
    private String domain;
    private String flashcardFront;
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
}
