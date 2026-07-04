package com.savpoint.savpoint.service.rawg;

import com.savpoint.savpoint.configurations.RawgClient;
import com.savpoint.savpoint.dtos.rawg.GameDetailsResponse;
import com.savpoint.savpoint.dtos.rawg.GameSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RawgService {

    @Value("${rawg.api.key}")
    private String rawgApiKey;

    private final RawgClient rawgClient;

    public RawgService(RawgClient rawgClient) {
        this.rawgClient = rawgClient;
    }

    public GameDetailsResponse getGame (String game) {
        GameSearchResponse searchResponse = rawgClient.restClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("search", game)
                        .queryParam("key", rawgApiKey)
                        .queryParam("page_size", 1)
                        .build())
                .retrieve()
                .body(GameSearchResponse.class);

        if (searchResponse != null && searchResponse.results() != null && !searchResponse.results().isEmpty()) {
            return searchResponse.results().get(0);
        }
        return null;
    }
}
