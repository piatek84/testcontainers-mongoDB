package com.example.testcontainers;

import com.example.testcontainers.model.Player;
import com.example.testcontainers.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataMongoTest
public class MongoDbApplicationV2Test {


    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    private PlayerService cut;

    @BeforeEach
    void setUp() {
        this.cut = new PlayerService(mongoTemplate);
    }


    @Test
    void test(){
        this.mongoTemplate.save(new Player("1", "Pete", "Smith"));
        this.mongoTemplate.save(new Player("2", "Mike", "Smith"));

        List<Player> result = cut.findAllPlayers();
        assertThat(result.size()).isEqualTo(2);
    }
}
