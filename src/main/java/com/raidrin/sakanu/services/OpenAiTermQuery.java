package com.raidrin.sakanu.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OpenAiTermQuery {
    private final OpenAiService openAiService;

    public TermResponse query(String domain, String term) {
        String systemContent = TaskMessageGenerator.generateTaskMessage(domain);
        if(term == null || term.trim().isEmpty()) {
            throw new IllegalArgumentException("Term cannot be null or empty");
        }
        String sanitizedTerm = term.trim().toLowerCase(Locale.ENGLISH);
        if(sanitizedTerm.length() >= 255) {
            throw new IllegalArgumentException("Term cannot be longer than 255 characters");
        }
        return getResponseFromOpenAI(domain, systemContent, sanitizedTerm);
    }

    private TermResponse getResponseFromOpenAI(String domain, String systemContent, String term) {
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system");
        systemMessage.setContent(systemContent);

        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole("user");
        userMessage.setContent(term.trim().toLowerCase(Locale.ENGLISH));

        ChatCompletionRequest chatCompletionRequest =
                ChatCompletionRequest.builder()
                        .model("gpt-4")
                        .messages(
                                List.of(
                                        systemMessage,
                                        userMessage
                                )
                        )
                        .temperature(1.2)
                        .build();
        ChatCompletionResult chatCompletionResult = openAiService.createChatCompletion(chatCompletionRequest);
        List<ChatCompletionChoice> chatCompletionChoices = chatCompletionResult.getChoices();
        if (chatCompletionChoices.size() != 1) {
            throw new NoChoiceFoundException("Expected 1 choice, got " + chatCompletionChoices.size());
        }
        ChatCompletionChoice chatCompletionChoice = chatCompletionChoices.get(0);
        String gptResponse = chatCompletionChoice.getMessage().getContent();
        // Testing purposes
//        try {
//            Files.writeString(
//                    Path.of("gpt_responses", domain + "-" + term + ".json"),
//                    gptResponse
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try {
            return getTermResponse(gptResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process gpt response", e);
        }
    }

    public TermResponse getTermResponse(String gptResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                gptResponse,
                TermResponse.class
        );
    }
}
