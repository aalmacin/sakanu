package com.raidrin.sakanu.repositories;

import com.raidrin.sakanu.entities.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
    Term findByDomainAndTerm(String domain, String term);
}