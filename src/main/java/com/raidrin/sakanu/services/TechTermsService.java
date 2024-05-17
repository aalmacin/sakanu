package com.raidrin.sakanu.services;

import com.raidrin.sakanu.entities.Term;
import com.raidrin.sakanu.repositories.TermRepository;
import com.raidrin.sakanu.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TechTermsService {
    private final OpenAiTermQuery openAiTermQuery;
    private final TermRepository termRepository;
    private final JwtService jwtService;

    public TermResponse getTechTerm(String domain, String term, String token) {
        return openAiTermQuery.query(domain, term);
    }

    @Transactional
    public void saveTerm(Term termEntity, String token) {
        String subClaim = jwtService.getSubClaim(token);
        termEntity.setUser(subClaim);

        termRepository.save(termEntity);
    }

    public Term findTerm(String domain, String term, String token) {
        String subClaim = jwtService.getSubClaim(token);
        return termRepository.findByDomainAndTermAndUser(domain, term.toLowerCase(Locale.ENGLISH), subClaim);
    }

    @Transactional
    public void deleteTerm(Term existingTerm, String token) {
        String subClaim = jwtService.getSubClaim(token);
        termRepository.deleteByIdAndUser(existingTerm.getId(), subClaim);
    }

    public Page<Term> findAllTerms(Pageable pageable, String token) {
        String subClaim = jwtService.getSubClaim(token);
        return termRepository.findAllByUser(subClaim, pageable);
    }

    public Term findById(Long id, String token) {
        String subClaim = jwtService.getSubClaim(token);
        return termRepository.findByIdAndUser(id, subClaim).orElse(null);
    }
}
