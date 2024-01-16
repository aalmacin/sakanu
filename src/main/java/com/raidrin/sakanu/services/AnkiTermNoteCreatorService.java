package com.raidrin.sakanu.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnkiTermNoteCreatorService {
    private final ObjectMapper objectMapper;
    private final AnkiConnectService ankiConnectService;

    public String addNote(String deckName, TermResponse termResponse) {
        ankiConnectService.createDeck(deckName);

        ObjectNode params = objectMapper.createObjectNode();
        ObjectNode note = objectMapper.createObjectNode();
        note.put("deckName", deckName);
        note.put("modelName", "Sakanu");

        note.put("fields", objectMapper.createObjectNode()
                .put("Text", termResponse.getFlashcardFront())
                .put("Extra", termResponse.getDescription())
                .put("simpleExplanation", termResponse.getSimpleExplanation())
                .put("purpose", termResponse.getPurpose())
                .put("questions", convertQuestionsToHtmlList(termResponse.getQuestions()))
                .put("relatedTerms", convertToHtmlList(termResponse.getRelatedTerms()))
                .put("categories", convertToHtmlList(termResponse.getCategories()))
        );

        params.put("note", note);

        return ankiConnectService.postToAnkiConnect("addNote", params);
    }

    public static String convertQuestionsToHtmlList(List<TermResponse.Question> items) {
        return convertToHtmlList(items.stream().map(item ->
                "<div class='question-li'>" +
                        "<div class='question'>" + item.getQuestion() + "</div>" +
                        "<div class='answer'>" + item.getAnswer() + "</div>" +
                        "</div>"
        ).collect(Collectors.toList()));
    }

    public static String convertToHtmlList(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "<ul></ul>"; // Empty list
        }

        return "<ul>" +
                items.stream()
                        .map(item -> "<li>" + item + "</li>")
                        .collect(Collectors.joining()) +
                "</ul>";
    }
}
