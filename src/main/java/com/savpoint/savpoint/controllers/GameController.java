package com.savpoint.savpoint.controllers;

import com.savpoint.savpoint.dtos.requests.GameSuggestionRequest;
import com.savpoint.savpoint.dtos.responses.GameSuggestionResponse;
import com.savpoint.savpoint.service.domain.GameService;
import com.savpoint.savpoint.service.domain.GameSuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final GameSuggestionService gameSuggestionService;

    public GameController(GameService gameService, GameSuggestionService gameSuggestionService) {
        this.gameService = gameService;
        this.gameSuggestionService = gameSuggestionService;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchGame(@RequestParam String name) {
        Object response = gameService.searchGame(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}/complete-info")
    public ResponseEntity<Object> completeGameInfo(@PathVariable String slug) {
        Object response = gameService.completeGameInfoBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/suggestions")
    public ResponseEntity<List<GameSuggestionResponse>> generateSuggestions(Authentication authentication, @RequestBody GameSuggestionRequest request) {
        List<GameSuggestionResponse> suggestions = gameSuggestionService.generateSuggestions(authentication, request.game(), request.experience());
        return ResponseEntity.ok(suggestions);
    }
}
