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

    public GameEntity completeGameInfoBySlug(String slug) {
        Optional<GameEntity> gameOpt = gameRepository.findBySlug(slug);

        if (gameOpt.isEmpty()) {
            throw new com.savpoint.savpoint.exceptions.NotFoundException("Jogo não encontrado no banco de dados local com o slug: " + slug);
        }

        GameEntity game = gameOpt.get();

        if (game.isCompletedInfo()) {
            return game;
        }

        GameDetailsResponse rawgGame = rawgService.findGameBySlug(slug);

        if (rawgGame != null) {
            String developers = rawgGame.developers() != null
                    ? rawgGame.developers().stream().map(d -> d.name()).collect(Collectors.joining(", "))
                    : "";

            String publishers = rawgGame.publishers() != null
                    ? rawgGame.publishers().stream().map(p -> p.name()).collect(Collectors.joining(", "))
                    : "";

            game.setDescription(rawgGame.description_raw());
            game.setDevelopers(developers);
            game.setPublisher(publishers);
            game.setCompletedInfo(true);

            return gameRepository.save(game);
        }

        throw new com.savpoint.savpoint.exceptions.NotFoundException("Não foi possível buscar detalhes do jogo no RAWG para o slug: " + slug);
    }
}
