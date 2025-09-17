package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AIServiceV2 {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private StreamingChatModel streamingChatModel;

    public Flux<String> textChat(String question) {
        return chatClient
                .prompt()
                .user(question)
                .stream()
                .content();


    }

    public Flux<String> streamChat(String question) {
        return streamingChatModel.stream(question);

    }

}
