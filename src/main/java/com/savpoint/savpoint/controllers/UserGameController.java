package com.savpoint.savpoint.controllers;

import com.savpoint.savpoint.dtos.requests.UserGameRequest;
import com.savpoint.savpoint.dtos.responses.MessageResponse;
import com.savpoint.savpoint.service.domain.UserGameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savpoint.savpoint.dtos.responses.UserGameResponse;
import java.util.List;

@RestController
@RequestMapping("/api/user-games")
public class UserGameController {

    private final UserGameService userGameService;

    public UserGameController(UserGameService userGameService) {
        this.userGameService = userGameService;
    }

    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addGameToProfile(Authentication authentication,
            @RequestBody @Valid UserGameRequest request) {
        MessageResponse messageResponse = userGameService.addGameToProfile(authentication, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserGameResponse>> listUserGames(Authentication authentication) {
        List<UserGameResponse> games = userGameService.listUserGames(authentication);
        return ResponseEntity.ok(games);
    }
}
