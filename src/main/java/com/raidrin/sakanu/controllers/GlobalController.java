package com.raidrin.sakanu.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raidrin.sakanu.entities.Term;
import com.raidrin.sakanu.exceptions.LimitReachedException;
import com.raidrin.sakanu.models.ProblemDetail;
import com.raidrin.sakanu.services.TechTermsService;
import com.raidrin.sakanu.services.TermResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api/global/learn")
@RequiredArgsConstructor
public class GlobalController {
    private final TechTermsService techTermsService;

    @GetMapping("/{term}")
    public Mono<TermResponse> getOpenAIResponse(@RequestParam(value = "domain", required = false) String domain,
                                                @PathVariable("term") String term) {
        System.out.println("Received global request for domain: " + domain + " and term: " + term);
        Term foundTerm = techTermsService.findTerm(domain, term);

        if (foundTerm != null) {
            try {
                return Mono.just(TermResponse.fromTerm(foundTerm));
            } catch (JsonProcessingException e) {
                return Mono.error(new RuntimeException("Failed to convert term entity to term response"));
            }
        }
        TermResponse termResponse = techTermsService.getTechTerm(domain, term);

        Term termEntity = null;
        try {
            termEntity = Term.fromTermResponse(termResponse);
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Failed to convert term response to term entity"));
        }

        return Mono.just(termEntity)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(techTermsService::saveTerm)
                .thenReturn(termResponse);
    }

    @ExceptionHandler(LimitReachedException.class)
    public ResponseEntity<ProblemDetail> handleLimitReachedException(LimitReachedException ex) {
        ProblemDetail problemDetail = new ProblemDetail(ex.getMessage());
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }
}

