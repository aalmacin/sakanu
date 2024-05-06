package com.raidrin.sakanu.services;

import com.raidrin.sakanu.entities.Term;
import com.raidrin.sakanu.repositories.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TechTermsService {
    private final OpenAiTermQuery openAiTermQuery;
    private final TermRepository termRepository;

    public TermResponse getTechTerm(String domain, String term) {
        return openAiTermQuery.query(domain, term);
    }

    public void saveTerm(Term termEntity) {
        termRepository.save(termEntity);
    }

    public Term findTerm(String domain, String term) {
        return termRepository.findByDomainAndTerm(domain, term.toLowerCase(Locale.ENGLISH));
    }
}
