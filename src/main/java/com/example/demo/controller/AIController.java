package com.example.demo.controller;

import com.example.demo.service.AiService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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

}
