package com.raidrin.sakanu.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Column(columnDefinition = "TEXT")
    private String questions;
    @Column(columnDefinition = "TEXT")
    private String relatedTerms;
    @Column(columnDefinition = "TEXT")
    private String categories;

    public static Term fromTermResponse(TermResponse termResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Term term = new Term();
        term.setTerm(termResponse.getSearchTerm());
        term.setDomain(termResponse.getDomain());
        term.setCloze(termResponse.getCloze());
        term.setDescription(termResponse.getDescription());
        term.setPurpose(termResponse.getPurpose());
        term.setSimpleExplanation(termResponse.getSimpleExplanation());
        term.setQuestions(objectMapper.writeValueAsString(termResponse.getQuestions()));
        term.setCategories(objectMapper.writeValueAsString(termResponse.getCategories()));
        term.setRelatedTerms(objectMapper.writeValueAsString(termResponse.getRelatedTerms()));
        return term;
    }
}
