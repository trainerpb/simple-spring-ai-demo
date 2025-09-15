package com.example.demo.controller;

import com.example.demo.service.AiService;
import org.springframework.http.MediaType;
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

}
