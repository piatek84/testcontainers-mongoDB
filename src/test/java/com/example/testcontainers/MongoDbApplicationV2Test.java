package com.example.testcontainers;

import com.example.testcontainers.model.Player;
import com.example.testcontainers.repository.PlayerRepository;
import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.example.testcontainers.Utils.toJsonObject;
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
    private PlayerRepository repository;

    @AfterEach
    void delete(){
        repository.deleteAll();
    }

    @Test
    void testPlayersApi() throws JSONException {
        //given
        Player player1 = new Player("1", "Duncan", "Idaho");
        Player player2 = new Player("2", "Paul", "Atreides");
        repository.save(player1);
        repository.save(player2);

        JSONArray expected = new JSONArray()
                .put(toJsonObject(player1))
                .put(toJsonObject(player2));

        //when
        Response response = given()
                .when().get(URL + this.port + "/players");


        //then
        JSONArray actual = new JSONArray(response.getBody().asString());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void testPlayerApi() throws JSONException {
        //given
        Player player1 = new Player("1", "Duncan", "Idaho");

        JSONObject expected = toJsonObject(player1);

        //when
        Response response =
                given()
                        .contentType(ContentType.JSON).
                        body(player1)
                .when()
                        .post(URL + this.port + "/player");


        //then
        JSONObject actual = new JSONObject(response.getBody().asString());
        JSONAssert.assertEquals(expected, actual, false);
    }
}
