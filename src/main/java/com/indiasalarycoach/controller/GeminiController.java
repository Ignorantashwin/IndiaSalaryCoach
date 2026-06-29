package com.indiasalarycoach.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.indiasalarycoach.service.GeminiService;

import lombok.RequiredArgsConstructor;

 @RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping("/gemini")
    public String testGemini() {
        return geminiService.askGemini(
                "Give me 3 Java interview questions only no answers no examples");
    }
}