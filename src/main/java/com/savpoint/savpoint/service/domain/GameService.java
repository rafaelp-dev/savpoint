package com.savpoint.savpoint.service.domain;

import com.savpoint.savpoint.entities.GameEntity;
import com.savpoint.savpoint.repositories.GameRepository;
import com.savpoint.savpoint.service.rawg.RawgService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final RawgService rawgService;

    public GameService(GameRepository gameRepository, RawgService rawgService) {
        this.gameRepository = gameRepository;
        this.rawgService = rawgService;
    }

    public Object searchGame(String gameName) {
        // Prepara a string para a busca: "zelda ocarina" -> "%zelda%ocarina%"
        String fuzzySearchTerm = "%" + gameName.trim().replaceAll("\\s+", "%") + "%";

        List<GameEntity> localGames = gameRepository.findTop3ByTitleLikeIgnoreCase(fuzzySearchTerm);

        if (!localGames.isEmpty()) {
            return localGames; // Retorna os 3 primeiros jogos do banco local
        }

        // Se não encontrar no banco local, faz a requisição na RAWG
        return rawgService.findTop3GameData(gameName);
    }
}
