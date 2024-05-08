package com.raidrin.sakanu.controllers;

import com.raidrin.sakanu.entities.Term;
import com.raidrin.sakanu.services.TechTermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermController {
    private final TechTermsService techTermsService;

    @GetMapping
    public Mono<ResponseEntity<Page<Term>>> getAllTerms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Mono.just(techTermsService.findAllTerms(PageRequest.of(page - 1, size)))
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{domain}/{term}")
    public Mono<ResponseEntity<Term>> getTerm(@PathVariable String domain, @PathVariable String term) {
        return Mono.justOrEmpty(techTermsService.findTerm(domain, term))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{domain}/{term}")
    public Mono<ResponseEntity<Void>> deleteTerm(@PathVariable String domain, @PathVariable String term) {
        return Mono.justOrEmpty(techTermsService.findTerm(domain, term))
                .doOnNext(techTermsService::deleteTerm)
                .thenReturn(ResponseEntity.noContent().build());
    }
}