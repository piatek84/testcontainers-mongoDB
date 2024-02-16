package com.example.testcontainers;

import com.example.testcontainers.model.Player;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MongoDbApplicationV2Test {

    public static final String URL = "http://localhost:";
    @LocalServerPort
    private int port;
    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void testPlayersApi() throws JSONException {
        //given
        Player player1 = new Player("1", "Pete", "Smith");
        Player player2 = new Player("2", "John", "Parker");
        Player player3 = new Player("3", "Antony", "Bond");
        this.mongoTemplate.save(player1);
        this.mongoTemplate.save(player2);
        this.mongoTemplate.save(player3);
        JSONArray expected = new JSONArray()
                .put(toJsonObject(player1))
                .put(toJsonObject(player2))
                .put(toJsonObject(player3));

        //when
        Response response = given()
                .when().get(URL + this.port + "/players");


        //then
        JSONArray actual = new JSONArray(response.getBody().asString());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @NotNull
    private static JSONObject toJsonObject(Player player) throws JSONException {
        return new JSONObject(new Gson().toJson(player));
    }
}
