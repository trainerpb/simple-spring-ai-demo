package com.example.demo.controller;

import com.example.demo.model.Batter;
import com.example.demo.service.AIServiceV2;
import com.example.demo.service.StructuredOutputAIService;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/chatV3")
@CrossOrigin("*")

public class AIControllerV3 {

    @Autowired
    private StructuredOutputAIService structuredOutputAIService;
    @GetMapping("/batter")
    public Batter getTopBatsmen() {
        return structuredOutputAIService.getTopBatter();
    }

}
