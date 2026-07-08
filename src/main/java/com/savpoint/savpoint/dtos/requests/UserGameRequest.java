package com.savpoint.savpoint.dtos.requests;

import com.savpoint.savpoint.enums.GameStatus;
import jakarta.validation.constraints.NotNull;

public record UserGameRequest(
        @NotNull(message = "O ID do jogo é obrigatório.")
        Long gameId,

        @NotNull(message = "O status do jogo é obrigatório.")
        GameStatus status,

        @NotNull(message = "Informar se é favorito é obrigatório.")
        Boolean favorite,

        @NotNull(message = "A nota (rating) é obrigatória.")
        Integer rating,

        String review
) {
}
