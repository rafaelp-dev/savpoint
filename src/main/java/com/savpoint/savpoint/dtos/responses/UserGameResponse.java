package com.savpoint.savpoint.dtos.responses;

import com.savpoint.savpoint.enums.GameStatus;
import java.time.LocalDateTime;

public record UserGameResponse(
                Long userGameId,
                Long gameId,
                String gameTitle,
                String gameCoverUrl,
                GameStatus status,
                Boolean favorite,
                Integer rating,
                String review,
                LocalDateTime addedAt) {
}
