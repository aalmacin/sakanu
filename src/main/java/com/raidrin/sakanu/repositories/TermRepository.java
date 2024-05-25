package com.raidrin.sakanu.repositories;

import com.raidrin.sakanu.entities.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {
    Term findByDomainAndTermAndUser(String domain, String term, String user);

    void deleteByIdAndUser(Long id, String user);

    Page<Term> findAllByUser(String user, Pageable pageable);

    Optional<Term> findByIdAndUser(Long id, String user);

    long countByUser(String user);
}