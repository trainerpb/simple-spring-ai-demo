package com.example.demo.service;

import com.example.demo.model.Batter;
import com.example.demo.model.Pair;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service
public class StructuredOutputAIService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    ChatModel chatModel;
    @Autowired
    private StreamingChatModel streamingChatModel;



    public Batter getTopBatter() {
        BeanOutputConverter<Batter> converter = new BeanOutputConverter<>(Batter.class);
        UserMessage message = new UserMessage(
                "Give me top 1 TEST batsman in the world.\n" + converter.getFormat()
        );

        Prompt prompt = new Prompt(List.of(message));
        ChatResponse response = chatModel.call(prompt);
        Generation generation = response.getResult();  // call getResults() if multiple generations
        return converter.convert(generation.getOutput().getText());


    }

//    public List<Batter> getTopBatters(int n) {
//        ConversionService conversionService = new DefaultConversionService();
//        ListOutputConverter converter = new ListOutputConverter(new DefaultConversionService());
//        UserMessage message = new UserMessage(
//                "Give me top 1 TEST batsman in the world.\n" + converter.getFormat()
//        );
//
//        Prompt prompt = new Prompt(List.of(message));
//        ChatResponse response = chatModel.call(prompt);
//        Generation generation = response.getResult();  // call getResults() if multiple generations
//        return converter.convert(generation.getOutput().getText());
//
//
//    }
}
