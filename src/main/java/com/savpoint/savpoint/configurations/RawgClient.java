package com.savpoint.savpoint.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RawgClient {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://api.rawg.io/api")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
