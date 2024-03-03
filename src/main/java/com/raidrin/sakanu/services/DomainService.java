package com.raidrin.sakanu.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomainService {
    private final AnkiConnectService ankiConnectService;

    @Cacheable(value = "domains")
    public List<String> getDomains() {
        return ankiConnectService.getDecks();
    }

    @CacheEvict(value = "domains", allEntries = true)
    public void createDomain(String deckName) {
        ankiConnectService.createDeck(deckName);
    }
}
