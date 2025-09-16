package com.example.demo.service;


import com.example.demo.config.dal.StudentDao;
import com.example.demo.model.Student;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.query.Param;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service

public class AiService {

    private final ChatClient chatClient;
    private final MarkDownSerevice markDownSerevice;

    @Autowired
    private StudentDao studentDao;


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



    public List<Student>  byMarks(int marks) {
        PromptTemplate promptTemplate = new PromptTemplate("fetch students who scored more than {marks} marks");
        promptTemplate.add("marks", String.valueOf(marks));
        Prompt prompt = new Prompt(List.of(
                new SystemMessage("You are a searching db for students based on marks"),
                new UserMessage(promptTemplate.render())
        ));

        return chatClient.prompt(prompt)
                .tools(studentDao)

                .call()

                .entity(new ParameterizedTypeReference<List<Student>>() {
                });

    }

    public Flux<String> tellMeFromTextFile(String  filePath, String question) throws IOException {
        String content = Files.readString(Path.of(filePath));
        Document doc = new Document(content);

        Prompt prompt = new Prompt(List.of(
                new SystemMessage("You are a helpful assistant that helps people find information from the provided document :"+content),
                new UserMessage(question),
                new AssistantMessage("Sure! Here's your quiz:")
        ));

        return chatClient
                .prompt(prompt)           // Start building the prompt

                .stream().content();


    }




}
