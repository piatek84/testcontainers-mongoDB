package com.example.testcontainers.service;

import com.example.testcontainers.model.Player;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final MongoTemplate mongoTemplate;

    public PlayerService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Player> findAllPlayers() {
        return mongoTemplate.findAll(Player.class);
    }
}