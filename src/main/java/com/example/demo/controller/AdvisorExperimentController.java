package com.example.demo.controller;


import com.example.demo.service.AdvisorExperimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/advisor")
@RequiredArgsConstructor
public class AdvisorExperimentController {

    private final AdvisorExperimentService advisorExperimentService;


    @GetMapping("/ask")
    public  Flux<String> ask(@RequestParam("lang") String lang, @RequestParam("q")String question){
        return advisorExperimentService.simpleChat(question,lang);
    }


}
