package com.example.demo.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Flux<String> ask(String promptText) {
      return
        chatClient
                .prompt()           // Start building the prompt
                .user(promptText)       // Add user message
                .stream()             // Send to AI model
                .content();
    }


}
