package com.savpoint.savpoint.service.rawg;

import com.savpoint.savpoint.configurations.RawgClient;
import com.savpoint.savpoint.dtos.rawg.GameDetailsResponse;
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
        return rawgClient.restClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("search", game)
                        .queryParam("key", rawgApiKey)
                        .queryParam("page_size", 1)
                        .build())
                .retrieve()
                .body(GameDetailsResponse.class);
    }
}
