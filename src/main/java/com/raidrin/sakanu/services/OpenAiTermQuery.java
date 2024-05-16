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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OpenAiTermQuery {
    private final OpenAiService openAiService;
    @Value("${openai.enabled}")
    private boolean pullFromOpenAI;

    public TermResponse query(String domain, String term) {
        String systemContent = TaskMessageGenerator.generateTaskMessage(domain);
        if (term == null || term.trim().isEmpty()) {
            throw new IllegalArgumentException("Term cannot be null or empty");
        }
        String sanitizedTerm = term.trim().toLowerCase(Locale.ENGLISH);
        if (sanitizedTerm.length() >= 255) {
            throw new IllegalArgumentException("Term cannot be longer than 255 characters");
        }
        if (pullFromOpenAI) {
            return getResponseFromOpenAI(systemContent, domain, sanitizedTerm);
        }
        return getMockResponse(systemContent, domain, sanitizedTerm);
    }

    private TermResponse getResponseFromOpenAI(String systemContent, String domain, String term) {
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system");
        systemMessage.setContent(systemContent);

        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole("user");
        userMessage.setContent(term.trim().toLowerCase(Locale.ENGLISH));

        ChatCompletionRequest chatCompletionRequest =
                ChatCompletionRequest.builder()
                        .model("gpt-4o")
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

        try {
            TermResponse termResponse = getTermResponse(gptResponse);
            termResponse.setDomain(domain);
            return termResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process gpt response", e);
        }
    }

    @NotNull
    private static TermResponse getMockResponse(String systemContent, String domain, String term) {
        TermResponse termResponse = new TermResponse();
        termResponse.setDomain(domain);
        termResponse.setSearchTerm(term);
        termResponse.setCloze("This is a test {{c1::flashcard}} front");
        termResponse.setDescription("This is a test description");
        termResponse.setPurpose("This is a test purpose");
        termResponse.setSimpleExplanation("This is a test simple explanation");

        TermResponse.Question question1 = new TermResponse.Question();
        question1.setQuestion("This is a test question 1");
        question1.setAnswer("This is a test answer 1");

        TermResponse.Question question2 = new TermResponse.Question();
        question2.setQuestion("This is a test question 2");
        question2.setAnswer("This is a test answer 2");

        termResponse.setQuestions(Arrays.asList(question1, question2));
        termResponse.setRelatedTerms(Arrays.asList("This is a test related term 1", "This is a test related term 2"));
        termResponse.setCategories(Arrays.asList("This is a test category 1", "This is a test category 2"));

        return termResponse;
    }

    public TermResponse getTermResponse(String gptResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                gptResponse,
                TermResponse.class
        );
    }
}
