package com.savpoint.savpoint.dtos.responses;

import java.util.List;

public record GameSuggestionResponse(
        String name,
        List<String> platforms,
        String similarity,
        String reason
) {
}
