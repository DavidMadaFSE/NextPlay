package com.david.nextplay.repository;

import com.david.nextplay.entity.Game;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    
    boolean existsByTitleIgnoreCaseAndReleaseDate(String title, LocalDate releaseDate);
}
