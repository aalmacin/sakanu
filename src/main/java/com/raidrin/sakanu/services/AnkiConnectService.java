package com.raidrin.sakanu.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnkiConnectService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${anki.connect.url}")
    private String ankiConnectUrl;


    public String postToAnkiConnect(String action, ObjectNode params) {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("action", action);
        requestBody.put("version", 6);
        requestBody.set("params", params);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        // Post request to Anki Connect
        return restTemplate.postForObject(ankiConnectUrl, request, String.class);
    }

    public String createDeck(String deckName) {
        ObjectNode params = objectMapper.createObjectNode();
        params.put("deck", deckName);
        return postToAnkiConnect("createDeck", params);
    }

    public List<String> getModelNames() {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("action", "modelNames");
        requestBody.put("version", 6);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        JsonNode response = restTemplate.postForObject(ankiConnectUrl, request, JsonNode.class);
        return objectMapper.convertValue(response.get("result"), new TypeReference<List<String>>() {
        });
    }
}

