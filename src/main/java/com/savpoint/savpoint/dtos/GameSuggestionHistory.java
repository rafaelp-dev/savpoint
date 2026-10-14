package com.savpoint.savpoint.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.savpoint.savpoint.dtos.responses.GameSuggestionResponse;

import java.time.LocalDateTime;
import java.util.List;

public record GameSuggestionHistory(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        String userEmail,
        String game,
        String experience,
        List<GameSuggestionResponse> suggestions
) {
}
