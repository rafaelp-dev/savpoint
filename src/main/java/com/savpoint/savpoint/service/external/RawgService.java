package com.savpoint.savpoint.service.external;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestClient;
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

    private final RestClient restClient;

    public RawgService(@Qualifier("rawgRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<GameDetailsResponse> findTop3GameData(String game) {
        GameSearchResponse searchResponse = restClient.get()
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

    public GameDetailsResponse findGameBySlug(String slug) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games/{slug}")
                        .queryParam("key", rawgApiKey)
                        .build(slug))
                .retrieve()
                .body(GameDetailsResponse.class);
    }
}
