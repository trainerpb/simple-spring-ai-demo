package com.example.demo.service.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoryService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public String statelessChat(String msg) {
        return chatClient
                .prompt().
                advisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).
                user(msg).call().content();
    }
}
