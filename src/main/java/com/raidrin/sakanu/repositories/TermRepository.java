package com.raidrin.sakanu.repositories;

import com.raidrin.sakanu.entities.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {
    Term findByDomainAndTermAndUser(String domain, String term, String user);

    void deleteByIdAndUser(Long id, String user);

    Page<Term> findAllByUser(String user, Pageable pageable);

    Optional<Term> findByIdAndUser(Long id, String user);

    @Query("SELECT COUNT(t) FROM Term t WHERE t.createdDate >= CURRENT_DATE AND t.user = :user")
    long countByCreatedDateTodayAndUser(String user);

    @Query("SELECT DISTINCT t.domain FROM Term t WHERE t.user = :user AND t.domain IS NOT NULL")
    List<String> getDomainsByUser(@Param("user")  String user, Pageable pageable);
}