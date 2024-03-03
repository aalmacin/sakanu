package com.raidrin.sakanu.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechTermsService {
    private final OpenAiTermQuery openAiTermQuery;


    @Cacheable(value = "term")
    public TermResponse getTechTerm(String domain, String term) {
        return openAiTermQuery.query(domain, term);
    }
}
