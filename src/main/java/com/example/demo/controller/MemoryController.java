package com.example.demo.controller;


import com.example.demo.service.memory.MemoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memory-chat")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class MemoryController {


    private final MemoryService memoryService;


    @GetMapping("/stateless")
    public  String statelessChat(@RequestParam("q") String msg){
        return  memoryService.statelessChat(msg);
    }
}

