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

    @GetMapping("/learn/{domain}/{term}")
    public Mono<TermResponse> getOpenAIResponse(@PathVariable("domain") String domain, @PathVariable("term") String term) {
        System.out.println("Received request for domain: " + domain + " and term: " + term);
        return Mono.just(techTermsService.getTechTerm(domain, term));
    }
}

