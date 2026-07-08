package com.savpoint.savpoint.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GroqClient {

    @Bean
    public RestClient groqRestClient () {
        return RestClient.builder()
                .baseUrl("https://api.groq.com/openai/v1/chat/completions")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
