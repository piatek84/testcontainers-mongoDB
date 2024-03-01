package com.example.testcontainers;

import com.example.testcontainers.model.Player;
import com.example.testcontainers.repository.PlayerRepository;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.datafaker.Faker;
import org.jetbrains.annotations.NotNull;
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
public class MongoDbApplicationTest {

    public static final String URL = "http://localhost:";
    public static final String PLAYERS = "/players";
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

    Faker faker = new Faker();
;
    @Test
    void shouldReturnAllPlayers() throws JSONException {
        //given
        Player player1 = generatePlayer();
        Player player2 = generatePlayer();

        repository.save(player1);
        repository.save(player2);

        JSONArray expected = new JSONArray()
                .put(toJsonObject(player1))
                .put(toJsonObject(player2));

        //when
        Response response = generateBaseGiven()
                .when()
                .get(PLAYERS);


        //then
        response.then().statusCode(200);
        JSONArray actual = new JSONArray(response.getBody().asString());
        JSONAssert.assertEquals(expected, actual, false);
    }

   @Test
    void shouldSavePlayer() throws JSONException {
        //given
        Player player1 = generatePlayer();

        JSONObject expected = toJsonObject(player1);

        //when
        Response response = generateBaseGiven()
                .body(player1)
                .when()
                .post(PLAYERS);

        //then
       response.then().statusCode(201);
       JSONObject actual = new JSONObject(response.getBody().asString());
       JSONAssert.assertEquals(expected, actual, false);
    }

    @NotNull
    private Player generatePlayer() {
        return new Player(faker.idNumber().peselNumber(), faker.name().firstName(), faker.name().lastName());
    }

    private RequestSpecification generateBaseGiven() {
        return given()
                .baseUri(URL + this.port)
                .contentType(ContentType.JSON);
    }
}
