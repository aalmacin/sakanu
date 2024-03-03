package com.raidrin.sakanu.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnkiConnectService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${anki.connect.url}")
    private String ankiConnectUrl;


    public List<String> getDecks() {
        ObjectNode requestBody = getRequestBody("deckNames");
        HttpEntity<String> request = getRequestHeaders(requestBody);
        JsonNode response = restTemplate.postForObject(ankiConnectUrl, request, JsonNode.class);
        return objectMapper.convertValue(response.get("result"), new TypeReference<List<String>>() { });
    }

    public String createDeck(String deckName) {
        ObjectNode params = objectMapper.createObjectNode();
        params.put("deck", deckName);
        return postToAnkiConnect("createDeck", params);
    }

    public List<String> getModelNames() {
        ObjectNode requestBody = getRequestBody("modelNames");

        HttpEntity<String> request = getRequestHeaders(requestBody);
        JsonNode response = restTemplate.postForObject(ankiConnectUrl, request, JsonNode.class);
        return objectMapper.convertValue(response.get("result"), new TypeReference<List<String>>() { });
    }

    public String postToAnkiConnect(String action, ObjectNode params) {
        ObjectNode requestBody = getRequestBody(action, params);
        HttpEntity<String> request = getRequestHeaders(requestBody);

        // Post request to Anki Connect
        return restTemplate.postForObject(ankiConnectUrl, request, String.class);
    }

    private ObjectNode getRequestBody(String action) {
        return getRequestBody(action, null);
    }

    private ObjectNode getRequestBody(String action, ObjectNode params) {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("action", action);
        requestBody.put("version", 6);
        if(params != null) {
            requestBody.set("params", params);
        }
        return requestBody;
    }

    private static HttpEntity<String> getRequestHeaders(ObjectNode requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestBody.toString(), headers);
    }
}

