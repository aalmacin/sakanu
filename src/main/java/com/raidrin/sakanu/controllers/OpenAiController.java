package com.raidrin.sakanu.controllers;

import com.raidrin.sakanu.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenAiController {
    private final TechTermsService techTermsService;
    private final AnkiSakanuModelCreatorService ankiSakanuModelCreatorService;
    private final AnkiTermNoteCreatorService ankiTermNoteCreatorService;

    @GetMapping("/test")
    public Mono<TermResponse> getOpenAIResponse(@RequestParam String domain, @RequestParam String term) {
        TermResponse termResponse = techTermsService.getTechTerm(domain, term);

        try {
            String response = ankiTermNoteCreatorService.addNote(domain, termResponse);
            System.out.println("Added note to Anki: " + response);
            return Mono.just(termResponse);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to add note to Anki"));
        }
    }

    @GetMapping("/anki-model")
    public Mono<String> createAnkiModel() {
        ankiSakanuModelCreatorService.createAnkiModel();

        return Mono.just("Created anki model");
    }
}

