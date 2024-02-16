package com.example.testcontainers.controller;

import com.example.testcontainers.model.Player;
import com.example.testcontainers.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerController {

    private final PlayerRepository repository;

    PlayerController(PlayerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/players")
    public List<Player> all() {
        return repository.findAll();
    }

    @PostMapping(path = "player",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> addPlayer(@RequestBody Player newPlayer) {
        Player player = repository.save(newPlayer);
        return new ResponseEntity<>(player, HttpStatus.CREATED);
    }
}
