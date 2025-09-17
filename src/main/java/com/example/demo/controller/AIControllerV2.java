package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.AIServiceV2;
import com.example.demo.service.AiService;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chatV2")
@CrossOrigin("*")

public class AIControllerV2 {

    @Autowired
    private  AIServiceV2 chatServiceV2;



    @GetMapping(path = {"/textChat"},produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> textChat(@RequestParam String prompt) {

        return (chatServiceV2.textChat(prompt));

    }


    @GetMapping(path = {"/streamChat"},produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam String prompt) {

        SseEmitter emitter = new SseEmitter(0L); // No timeout


        Flux<String> stream = chatServiceV2.streamChat(prompt);

        stream.subscribe(
                message -> {
                    try {
                        emitter.send(SseEmitter.event()
                                .name("message")
                                .data(message).build());
                        System.out.println("AIControllerV2.streamChat sent message: " + message);
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                },
                error -> emitter.completeWithError(error),
                () -> {
                    try {
                        emitter.send(SseEmitter.event()
                                .name("done")
                                .data("Stream complete").build());
                        System.out.println("AIControllerV2.streamChat sent done event" );
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }finally {
                        emitter.complete();
                        System.out.println("AIControllerV2.streamChat= complete" );
                    }

                }
        );

        return emitter;


    }
    // add controller for image generation
    @GetMapping("/generateImage")
    public ImageResponse generateImage(@RequestParam String prompt) {
        return chatServiceV2.generateImage(prompt);
    }
//    @GetMapping(value = "/doc", produces = "text/markdown")
    @GetMapping(value = "/doc", produces = MediaType.TEXT_HTML_VALUE)
    public String getMarkdownDoc() {
        String markdown = "# Hello\nThis is a **Markdown** response.";
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(markdown));

    }

}
