package com.example.demo.service;


import com.example.demo.service.advisor.MyOwnTokenCountAdvisor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvisorExperimentService {


    private final ChatClient chatClient;

    public Flux<String> simpleChat(String question, String programmingLanguage){



        return chatClient.prompt().system(s-> s.text("You are an expert assistant in coding with {lang}").param("lang",programmingLanguage))
                .user(question)
                .advisors(new SimpleLoggerAdvisor() , new SafeGuardAdvisor(List.of("communal","bomb","poison","suicide")), new MyOwnTokenCountAdvisor()) // Adding advisors
                .stream().content();


    }

}
