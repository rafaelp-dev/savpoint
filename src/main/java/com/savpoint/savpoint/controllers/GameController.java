package com.savpoint.savpoint.controllers;

import com.savpoint.savpoint.service.domain.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchGame(@RequestParam String name) {
        Object response = gameService.searchGame(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}/complete-info")
    public ResponseEntity<Object> completeGameInfo(@org.springframework.web.bind.annotation.PathVariable String slug) {
        Object response = gameService.completeGameInfoBySlug(slug);
        return ResponseEntity.ok(response);
    }
}
