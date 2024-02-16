package com.example.testcontainers.repository;

import com.example.testcontainers.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {

}