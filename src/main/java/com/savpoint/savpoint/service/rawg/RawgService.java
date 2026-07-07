package com.savpoint.savpoint.service.rawg;

import com.savpoint.savpoint.configurations.RawgClient;
import com.savpoint.savpoint.dtos.rawg.GameDetailsResponse;
import com.savpoint.savpoint.dtos.rawg.GameSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RawgService {

    @Value("${rawg.api.key}")
    private String rawgApiKey;

    private final RawgClient rawgClient;

    public RawgService(RawgClient rawgClient) {
        this.rawgClient = rawgClient;
    }

    public List<GameDetailsResponse> findTop3GameData(String game) {
        GameSearchResponse searchResponse = rawgClient.restClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("search", game)
                        .queryParam("key", rawgApiKey)
                        .queryParam("page_size", 3)
                        .build())
                .retrieve()
                .body(GameSearchResponse.class);

        if (searchResponse != null && searchResponse.results() != null && !searchResponse.results().isEmpty()) {
            return searchResponse.results();
        }
        return Collections.emptyList();
    }
}
