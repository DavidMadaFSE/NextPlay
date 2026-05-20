package com.david.nextplay.service;

import com.david.nextplay.dto.CreateGameRequest;
import com.david.nextplay.dto.GameResponse;
import com.david.nextplay.entity.Game;
import com.david.nextplay.enums.Genre;
import com.david.nextplay.enums.Platform;
import com.david.nextplay.exception.GameAlreadyExistsException;
import com.david.nextplay.repository.GameRepository;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameResponse> getAllGames() {
        List<Game> games = gameRepository.findAll();

        return games.stream()
                .map(this::mapToGameResponse)
                .toList();
    }

    public GameResponse getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game with ID " + id + " not found."));

        return mapToGameResponse(game);
    }

    public GameResponse createGame(CreateGameRequest request) {
        if (gameRepository.existsByTitleIgnoreCaseAndReleaseDate(request.getTitle(), request.getReleaseDate())) {
            throw new GameAlreadyExistsException("Game with title '" + request.getTitle() + "' and release date "
                    + request.getReleaseDate() + " already exists.");
        }

        Game newGame = new Game();

        newGame.setTitle(request.getTitle());
        newGame.setDescription(request.getDescription());
        newGame.setReleaseDate(request.getReleaseDate());
        newGame.setCoverImageUrl(request.getCoverImageUrl());
        newGame.setGenres(request.getGenres());
        newGame.setPlatforms(request.getPlatforms());
        newGame.setAverageRating(0.0);

        Game savedGame = gameRepository.save(newGame);

        return mapToGameResponse(savedGame);
    }

    public GameResponse updateGame(Long id, CreateGameRequest request) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game with ID " + id + " not found."));

        game.setTitle(request.getTitle());
        game.setDescription(request.getDescription());
        game.setReleaseDate(request.getReleaseDate());
        game.setCoverImageUrl(request.getCoverImageUrl());
        game.setGenres(request.getGenres());
        game.setPlatforms(request.getPlatforms());

        gameRepository.save(game);

        return mapToGameResponse(game);
    }

    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game with ID " + id + " not found."));

        gameRepository.delete(game);
    }

    public List<GameResponse> searchGames(String title, LocalDate releaseDate, Genre genre, Platform platform) {
        List<Game> games = gameRepository.findAll();

        return games.stream()
                .filter(game -> title == null || title.isBlank()
                        || game.getTitle().toLowerCase().contains(title.toLowerCase()))

                .filter(game -> releaseDate == null
                        || game.getReleaseDate().equals(releaseDate))

                .filter(game -> genre == null
                        || game.getGenres().contains(genre))

                .filter(game -> platform == null
                        || game.getPlatforms().contains(platform))

                .map(this::mapToGameResponse)
                .toList();
    }

    private GameResponse mapToGameResponse(Game game) {
        return new GameResponse(
                game.getId(),
                game.getTitle(),
                game.getDescription(),
                game.getReleaseDate(),
                game.getCoverImageUrl(),
                game.getAverageRating(),
                game.getGenres(),
                game.getPlatforms());
    }
}
