package com.example.demo.service;


import com.example.demo.service.advisor.MyOwnTokenCountAdvisor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvisorExperimentService {


    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public Flux<String> simpleChat(String question, String programmingLanguage) {

        log.info("chat Memory impl :{}", chatMemory.getClass().getName());

        var memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return chatClient.prompt().system(s -> s.text("You are an expert assistant in coding with {lang}").param("lang", programmingLanguage))
                .user(question)
                .advisors(memoryAdvisor, new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("communal", "bomb", "poison", "suicide"))/*, new MyOwnTokenCountAdvisor()*/) // Adding advisors
                .stream().content();


    }

    public String simpleChat_Blocking(String question) {
        var memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return chatClient.prompt().user(question)
                .advisors(memoryAdvisor)
                .call().content();
    }

}
