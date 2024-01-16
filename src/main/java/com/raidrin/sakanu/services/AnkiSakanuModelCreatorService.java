package com.raidrin.sakanu.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnkiSakanuModelCreatorService {
    private final AnkiConnectService ankiConnectService;

    private static final String CSS = """
            .back, .front {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f4f4f8;
                color: #333;
                padding: 20px;
                margin: 0;
                height: 100vh; /* Full viewport height */
                overflow-y: auto; /* Scroll for long content */
            }
                        
            .front, .back-front {
                font-size: 1em;
                color: #2a2a2a; /* Dark color for emphasis */
                font-weight: bold;
                margin-bottom: 15px;
                padding-bottom: 10px;
                border-bottom: 3px solid #4a8eda; /* Blue accent border */
            }
                        
            .description {
                background-color: #e9effb; /* Soft blue background */
                color: #2a2a2a;
                padding: 15px;
                margin-top: 10px;
                border-radius: 5px;
                box-shadow: 0px 2px 5px 0px rgba(0,0,0,0.1); /* Subtle shadow */
                overflow-y: auto; /* Enable scroll if content is long */
                max-height: 300px; /* Adjustable based on content size */
            }
                        
            /* Base Styles */
            .back {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f4f4f8;
                color: #333;
                padding: 20px;
                height: 100vh;
                overflow-y: auto;
            }
                        
            /* Headers */
            .simpleExplanationHeader, .purposeHeader, .questionsHeader, .relatedTermsHeader, .categoriesHeader {
                font-size: 1.3em;
                color: #5a5a5a; /* Subtle and elegant header color */
                margin-top: 30px;
                margin-bottom: 15px;
                border-bottom: 2px solid #4a8eda; /* Underline effect */
            }
                        
            /* Content Blocks */
            .simpleExplanation, .purpose {
                background-color: #e9effb; /* Soft blue background */
                color: #2a2a2a;
                padding: 15px;
                border-left: 6px solid #4a8eda; /* Blue accent border */
                border-radius: 5px;
                box-shadow: 0px 2px 5px 0px rgba(0, 0, 0, 0.1); /* Subtle shadow */
            }
                        
            /* Questions Section */
            .questions ul {
                list-style-type: none;
                padding: 0;
            }
                        
            .questions li {
                background-color: #f8f0df; /* Warm background for questions */
                margin-bottom: 10px;
                padding: 10px;
                border-radius: 5px;
                border-left: 4px solid #f2994a; /* Orange border for questions */
                box-shadow: 0px 2px 5px 0px rgba(0, 0, 0, 0.1);
            }
                        
            .question-li {
                background-color: #f9f6f2;
                padding: 8px;
                margin-bottom: 5px;
                border: 1px solid #e0e0e0; /* Light border for the question-answer pair */
            }
                        
            .question {
                font-weight: bold;
                /* Emphasize the question /
               font-size: 1.1em; / Larger text for questions /
               color: #d35400; / A vibrant color for questions */
                margin-bottom: 5px;
            }
                        
            .answer {
                font-size: 1em;
                /* Standard text size for answers /
               color: #7f8c8d; / Subdued color for answers */
                margin-bottom: 5px;
            }
                        
            /* Related Terms and Categories Styles /
            .relatedTerms ul, .categories ul {
            list-style-type: none; / Remove default list styling */
            padding: 0 ;
            }
                        
            .relatedTerms li, .categories li {
                background-color: #ecf0f1;
                /* Light background for terms and categories /
               margin-bottom: 5px;
               padding: 10px;
               border-radius: 5px;
               border-left: 4px solid #3498db; / Bright blue border for related terms and categories */
                box-shadow: 0px 2px 5px 0px rgba(0, 0, 0, 0.1);
            }
            """;

    public void createAnkiModel() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode template = objectMapper.createObjectNode();
        template.put("Name", "Sakanu");
        template.put("Front", "<div class='front'>{{cloze:Text}}</div>");
        template.put("Back", "<div class='back'>" + "<div class='back-front'>{{cloze:Text}}</div>" + "<div class='description'>{{Extra}}</div>" + "<h2 class='simpleExplanationHeader'>Simple Explanation</h2>" + "<div class='simpleExplanation'>" + "{{simpleExplanation}}" + "</div>" + "<h2 class='purposeHeader'>Purpose</h2>" + "<div class='purpose'>" + "{{purpose}}" + "</div>" + "<h2 class='questionsHeader'>Questions</h2>" + "<div class='questions'>" + "{{questions}}" + "</div>" + "<h2 class='relatedTermsHeader'>Related Terms</h2>" + "<div class='relatedTerms'>" + "{{relatedTerms}}" + "</div>" + "<h2 class='categoriesHeader'>Categories</h2>" + "<div class='categories'>" + "{{categories}}" + "</div>" + "</div>");
        ArrayNode cardTemplates = objectMapper.createArrayNode();
        cardTemplates.add(template);

        ObjectNode params = objectMapper.createObjectNode();
        params.put("modelName", "Sakanu");

        ArrayNode fieldsArray = objectMapper.createArrayNode();
        List<String> fieldNames = List.of("Text", "Extra", "purpose", "simpleExplanation", "questions", "relatedTerms", "categories");
        fieldNames.forEach(fieldsArray::add);
        params.set("inOrderFields", fieldsArray);

        params.put("css", ".card {" + " font-family: arial;" + " font-size: 20px;" + " text-align: center;" + " color: black;" + " background-color: white;" + "}" + ".cloze {" + " font-weight: bold;" + " color: blue;" + "}");

        params.put("isCloze", true);

        params.set("cardTemplates", cardTemplates);

        params.put("css", CSS);

        String model = ankiConnectService.postToAnkiConnect("createModel", params);
        System.out.println(model);
    }
}
