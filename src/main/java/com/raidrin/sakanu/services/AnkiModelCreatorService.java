package com.raidrin.sakanu.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnkiModelCreatorService {
    private final AnkiConnectService ankiConnectService;

    public void createAnkiModel() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode template = objectMapper.createObjectNode();
        template.put("Name", "Sakanu");
        template.put("Front", "<div class='front'>{{cloze:Text}}</div>");
        template.put("Back", "<div class='back'>" +
                "<div class='back-front'>{{cloze:Text}}</div>" +
                "<div class='description'>{{Extra}}</div>" +
                "<h2 class='simpleExplanationHeader'>Simple Explanation</h2>" +
                "<div class='simpleExplanation'>" + "{{simpleExplanation}}" + "</div>" +
                "<h2 class='purposeHeader'>Purpose</h2>" +
                "<div class='purpose'>" + "{{purpose}}" + "</div>" +
                "<h2 class='questionsHeader'>Questions</h2>" +
                "<div class='questions'>" + "{{questions}}" + "</div>" +
                "<h2 class='relatedTermsHeader'>Related Terms</h2>" +
                "<div class='relatedTerms'>" + "{{relatedTerms}}" + "</div>" +
                "<h2 class='categoriesHeader'>Categories</h2>" +
                "<div class='categories'>" + "{{categories}}" + "</div>" +
                "</div>");
        ArrayNode cardTemplates = objectMapper.createArrayNode();
        cardTemplates.add(template);
        String model = ankiConnectService.createModel(
                "Sakanu",
                List.of("Text", "Extra", "flashcardFront", "purpose", "simpleExplanation", "questions", "relatedTerms", "categories"),
                cardTemplates
        );
        System.out.println(model);
    }
}
