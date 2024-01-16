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

@Service
@RequiredArgsConstructor
public class AnkiConnectService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${anki.connect.url}")
    private String ankiConnectUrl;

    private String CSS = """
                    .simpleExplanationHeader, .purposeHeader, .questionsHeader, .relatedTermsHeader, .categoriesHeader {
                        font-size: 1.2em;
                        color: #FFC107; /* Distinct color for headers */
                        margin-top: 20px;
                        margin-bottom: 10px;
                    }
                                
                    .simpleExplanation, .purpose {
                        background-color: #555;
                        color: #FFF;
                        padding: 10px;
                        border-left: 5px solid #2196F3; /* Different border color for distinction */
                    }
                                
                    .questions, .relatedTerms, .categories {
                        background-color: #666;
                        padding: 10px;
                        border-left: 5px solid #9C27B0; /* Purple border for these sections */
                    }
                                
                    ul {
                        list-style-type: none; /* Remove default list styling */
                        padding: 0;
                    }
                                
                    li {
                        background-color: #777;
                        margin-bottom: 5px;
                        padding: 8px;
                        border-radius: 4px;
                    }
            """;


    public String addNote(String deckName, String front, String back) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construct the request body
        String requestBody = constructAddNoteRequestBody(deckName, front, back);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Post request to Anki Connect
        return restTemplate.postForObject(ankiConnectUrl, request, String.class);
    }

    private String constructAddNoteRequestBody(String deckName, String front, String back) {
        // Construct JSON request body according to Anki Connect API documentation
        return "{...}"; // Replace with actual JSON structure
    }

    public String createModel(String modelName, List<String> fieldNames, ArrayNode cardTemplates) {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("action", "createModel");
        requestBody.put("version", 6);

        ObjectNode params = objectMapper.createObjectNode();
        params.put("modelName", modelName);

        ArrayNode fieldsArray = objectMapper.createArrayNode();
        fieldNames.forEach(fieldsArray::add);
        params.set("inOrderFields", fieldsArray);

        params.put("css", ".card {" +
                " font-family: arial;" +
                " font-size: 20px;" +
                " text-align: center;" +
                " color: black;" +
                " background-color: white;" +
                "}" +
                ".cloze {" +
                " font-weight: bold;" +
                " color: blue;" +
                "}");

        params.put("isCloze", true);

        params.set("cardTemplates", cardTemplates);

        params.put("css", CSS);

        requestBody.set("params", params);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        System.out.println("Request body:");
        System.out.println(requestBody);
        System.out.println("End request body");

        return restTemplate.postForObject(ankiConnectUrl, request, String.class);
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

