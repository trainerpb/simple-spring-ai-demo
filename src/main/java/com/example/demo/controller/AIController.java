package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.AiService;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/chat")
@CrossOrigin("*")

public class AIController {

    private final AiService chatService;

    public AIController(AiService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam String prompt) {
       return (chatService.ask(prompt));
    }


    @GetMapping(path="/pt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithPrompt(@RequestParam String promptText) {
        return (chatService.askUsingPrompt(promptText));
    }

    @GetMapping(path="/mpt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithDiiferentPrompts(@RequestParam int n) {
        return (chatService.askUsingDifferentPrompts(n));
    }

    @GetMapping(path="/mpt2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatWithDiiferentPromptsV2(@RequestParam int n) {
        return (chatService.askUsingDifferentPrompts_UsingEfficientBuffering(n));
    }

    @GetMapping(path="/minMarks", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public List<Student> getStudentsWithMinMarks(@RequestParam int marks) {
     return  chatService.byMarks(marks);
    }

    @GetMapping(path="/story", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> tellMeFromTextFile(@RequestParam String question) throws IOException {
        return chatService.tellMeFromTextFile("C:\\data\\a.txt", question);


    }

}
