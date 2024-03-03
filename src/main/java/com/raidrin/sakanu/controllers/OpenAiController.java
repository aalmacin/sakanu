package com.raidrin.sakanu.controllers;

import com.raidrin.sakanu.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenAiController {
    private final TechTermsService techTermsService;
    private final AnkiSakanuModelCreatorService ankiSakanuModelCreatorService;
    private final AnkiTermNoteCreatorService ankiTermNoteCreatorService;
    private final DomainService domainService;

    @GetMapping("/learn/{domain}/{term}")
    public Mono<TermResponse> getOpenAIResponse(@PathVariable("domain") String domain, @PathVariable("term") String term) {
        System.out.println("Received request for domain: " + domain + " and term: " + term);
        TermResponse termResponse = techTermsService.getTechTerm(domain, term);

        try {
            String response = ankiTermNoteCreatorService.addNote(domain, termResponse);
            System.out.println("Added note to Anki: " + response);
            return Mono.just(termResponse);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to add note to Anki"));
        }
    }

    @GetMapping("domains")
    public Mono<List<String>> getDomains() {
        return Mono.just(domainService.getDomains());
    }
}

