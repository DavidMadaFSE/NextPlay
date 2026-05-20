package com.david.nextplay.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.david.nextplay.dto.CreateGameRequest;
import com.david.nextplay.dto.GameResponse;
import com.david.nextplay.enums.Genre;
import com.david.nextplay.enums.Platform;
import com.david.nextplay.service.GameService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGameById(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGameById(id));
    }

    @PostMapping
    public ResponseEntity<GameResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
        GameResponse createdGame = gameService.createGame(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdGame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> updateGame(@PathVariable Long id,
            @Valid @RequestBody CreateGameRequest request) {
        return ResponseEntity.ok(gameService.updateGame(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GameResponse>> searchGame(@RequestParam(required = false) String title, @RequestParam(required = false) LocalDate releaseDate,
            @RequestParam(required = false) Genre genre, @RequestParam(required = false) Platform platform) {
        return ResponseEntity.ok(gameService.searchGames(title, releaseDate, genre, platform));
    }
}
