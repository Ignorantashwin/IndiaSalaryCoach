package com.indiasalarycoach.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroqService {

    private final ObjectMapper objectMapper;
 private final RestTemplate restTemplate;

@Value("${groq.api-key}")
     private String apiKey;

 @Value ("${groq.model}")
     private String model;
     

  public String analyze(String prompt){


    System.out.println("GROQ KEY = " + apiKey);
System.out.println("MODEL = " + model);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

     Map <String,Object> body = new HashMap<>();
     body.put("model", model);
     body.put("messages", List.of(
        Map.of(
            "role", "user",
            "content", prompt
        )
     ));

     HttpEntity <Map<String, Object>> request = new HttpEntity<>(body, headers);
     String response = restTemplate.postForObject(
        "https://api.groq.com/openai/v1/chat/completions",
        request,
        String.class
);
System.out.println("RAW GROQ RESPONSE:");
System.out.println(response);

try {

  JsonNode root = objectMapper.readTree(response);

JsonNode choices = root.path("choices");

if (!choices.isArray() || choices.isEmpty()) {
    throw new RuntimeException("Invalid Groq response");
}

String content = choices.get(0)
        .path("message")
        .path("content")
        .asText();
        
    content = content
            .replace("```json", "")
            .replace("```", "")
            .trim();

    return content;

} catch (Exception e) {

    e.printStackTrace();
    return response;
}
  }
   
}
