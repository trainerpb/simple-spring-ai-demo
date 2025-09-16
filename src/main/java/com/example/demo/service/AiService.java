package com.example.demo.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service

public class AiService {

    private final ChatClient chatClient;
    private final MarkDownSerevice markDownSerevice;


    public AiService(ChatClient chatClient, MarkDownSerevice markDownSerevice) {
        this.chatClient = chatClient;
        this.markDownSerevice = markDownSerevice;
    }

    public Flux<String> ask(String promptText) {
      return
        chatClient
                .prompt()           // Start building the prompt
                .user(promptText)       // Add user message
                .stream()             // Send to AI model
                .content().map(markDownSerevice::parseMarkdown);
    }


    public Flux<String> askUsingPrompt(String promptText) {
        PromptTemplate promptTemplate = new PromptTemplate("you are a Bengali and English translator. translate the following text to Bengali: {text}");
        promptTemplate.add("text", promptText);
        return chatClient
                .prompt(promptTemplate.render())           // Start building the prompt

                .stream().content();


    }

    public Flux<String> askUsingDifferentPrompts(int promptText) {
        PromptTemplate promptTemplate = new PromptTemplate("\"Create a {num}-question quiz on Spring Boot.\"");
        promptTemplate.add("num", promptText);
        Prompt prompt = new Prompt(List.of(
                new SystemMessage("You are a student appearing an interview on Spring boot."),
                new UserMessage(promptTemplate.render()),
                new AssistantMessage("Sure! Here's your quiz:")
        ));

        return chatClient
                .prompt(promptTemplate.render())           // Start building the prompt

                .stream().content();
    }

    public Flux<ServerSentEvent<String>> askUsingDifferentPrompts_UsingEfficientBuffering(int promptText) {
        PromptTemplate promptTemplate = new PromptTemplate("\"Create a {num}-question quiz on Spring Boot.\"");
        promptTemplate.add("num", promptText);
        Prompt prompt = new Prompt(List.of(
                new SystemMessage("You are a student appearing an interview on Spring boot."),
                new UserMessage(promptTemplate.render()),
                new AssistantMessage("Sure! Here's your quiz:")
        ));

        return chatClient.prompt(prompt).stream().content()
                .filter(chunk -> chunk != null && !chunk.isBlank())
                .bufferUntil(chunk -> chunk.endsWith(" ") || chunk.matches("^[.,!?\\n]"))
                .map(buffer -> String.join("", buffer))
                .map(chunk -> ServerSentEvent.builder(chunk).build());


    }
}
