package com.raidrin.sakanu.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechTermsService {
    private final OpenAiTermQuery openAiTermQuery;


    public TermResponse getTechTerm(String domain, String term) {
        return openAiTermQuery.query(domain, term);
    }
}
