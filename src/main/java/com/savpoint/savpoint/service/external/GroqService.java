package com.savpoint.savpoint.service.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import java.util.List;

import com.savpoint.savpoint.dtos.responses.GroqResponse;

import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final RestClient restClient;

    public GroqService(@Qualifier("groqRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public GroqResponse sendPrompt(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", "openai/gpt-oss-120b",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                )
        );

        JsonNode response = restClient.post()
                .header("Authorization", "Bearer " + groqApiKey)
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);

        if (response != null && response.has("choices") && response.get("choices").isArray() && !response.get("choices").isEmpty()) {
            return new GroqResponse(response.get("choices").get(0).get("message").get("content").asText());
        }

        return null;
    }
}
