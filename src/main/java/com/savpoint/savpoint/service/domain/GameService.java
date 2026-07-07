package com.savpoint.savpoint.service.domain;

import com.savpoint.savpoint.dtos.rawg.GameDetailsResponse;
import com.savpoint.savpoint.entities.GameEntity;
import com.savpoint.savpoint.repositories.GameRepository;
import com.savpoint.savpoint.service.rawg.RawgService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final RawgService rawgService;

    public GameService(GameRepository gameRepository, RawgService rawgService) {
        this.gameRepository = gameRepository;
        this.rawgService = rawgService;
    }

    public Object searchGame(String gameName) {
        String fuzzySearchTerm = "%" + gameName.trim().replaceAll("\\s+", "%") + "%";

        List<GameEntity> localGames = gameRepository.findTop3ByTitleLikeIgnoreCase(fuzzySearchTerm);

        if (!localGames.isEmpty()) {
            return localGames;
        }

        List<GameDetailsResponse> rawgGames = rawgService.findTop3GameData(gameName);
        List<GameEntity> gamesToReturn = new ArrayList<>();

        for (GameDetailsResponse rawgGame : rawgGames) {
            Optional<GameEntity> existingGame = gameRepository.findByExternalId(rawgGame.id());

            if (existingGame.isPresent()) {
                gamesToReturn.add(existingGame.get());
            } else {
                String platforms = rawgGame.platforms() != null
                        ? rawgGame.platforms().stream().map(p -> p.platform().name()).collect(Collectors.joining(", "))
                        : "";

                String genres = rawgGame.genres() != null
                        ? rawgGame.genres().stream().map(g -> g.name()).collect(Collectors.joining(", "))
                        : "";

                LocalDate releaseDate = LocalDate.now();
                if (rawgGame.released() != null && !rawgGame.released().isEmpty()) {
                    releaseDate = LocalDate.parse(rawgGame.released());
                }

                GameEntity newGame = new GameEntity(
                        rawgGame.id(),
                        rawgGame.slug() != null ? rawgGame.slug() : rawgGame.id(),
                        rawgGame.name(),
                        null,
                        releaseDate,
                        rawgGame.background_image() != null ? rawgGame.background_image() : "",
                        rawgGame.playtime(),
                        platforms,
                        genres,
                        null,
                        null);

                gamesToReturn.add(gameRepository.save(newGame));
            }
        }

        return gamesToReturn;
    }
}
