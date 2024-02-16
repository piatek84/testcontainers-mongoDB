package com.example.testcontainers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataMongoTest
public class MongoDbApplicationTest {


    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    public static final String DATABASE_NAME = "test";
    public static final String PLAYERS = "players";

    @Test
    void databaseIsRunningWithExpectedNumberOfPlayers(){
        //given
        MongoClient mongoClient = setupDatabase();

        assertThat(mongoClient.getDatabase(DATABASE_NAME).getCollection(PLAYERS).countDocuments())
                .isEqualTo(2);
    }

    @NotNull
    private static MongoClient setupDatabase() {
        MongoClient mongoClient = MongoClients.create(mongoDBContainer.getConnectionString());
        mongoClient.getDatabase(DATABASE_NAME).createCollection(PLAYERS);
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(PLAYERS);

        List<Document> players = new ArrayList<>();
        players.add(Document.parse("{'name': 'Duncan', surname: 'Idaho' }"));
        players.add(Document.parse("{'name': 'Paul', surname: 'Atreides' }"));
        collection.insertMany(players);
        return mongoClient;
    }
}
