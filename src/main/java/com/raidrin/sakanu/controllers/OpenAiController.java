package com.raidrin.sakanu.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raidrin.sakanu.entities.Term;
import com.raidrin.sakanu.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenAiController {
    private final TechTermsService techTermsService;

    @GetMapping("/learn/{domain}/{term}")
    public Mono<TermResponse> getOpenAIResponse(@PathVariable("domain") String domain,
                                                @PathVariable("term") String term,
                                                @RequestHeader("Authorization") String token) {
        System.out.println("Received request for domain: " + domain + " and term: " + term);
        Term foundTerm = techTermsService.findTerm(domain, term, token);

        if (foundTerm != null) {
            try {
                return Mono.just(TermResponse.fromTerm(foundTerm));
            } catch (JsonProcessingException e) {
                return Mono.error(new RuntimeException("Failed to convert term entity to term response"));
            }
        }
        TermResponse termResponse = techTermsService.getTechTerm(domain, term, token);

        Term termEntity = null;
        try {
            termEntity = Term.fromTermResponse(termResponse);
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Failed to convert term response to term entity"));
        }

        return Mono.just(termEntity)
                .doOnNext(t -> techTermsService.saveTerm(t, token))
                .thenReturn(termResponse);
    }
}

