package com.example.testcontainers.controller;

import com.example.testcontainers.model.Player;
import com.example.testcontainers.repository.PlayerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerController {

    private final PlayerRepository repository;

    PlayerController(PlayerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/players")
    List<Player> all() {
        return repository.findAll();
    }
}
