package br.com.devwell.tasks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;

import static io.restassured.RestAssured.given;

public class ApiTest {

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI="http://localhost:8001/tasks-backend";
    }
    @Test
    public void shouldReturnTask(){
        given()
                .when()
                .get("/todo")
                .then().statusCode(200);
    }

    @Test
    public void shouldSaveTask(){
        given()
                .contentType(ContentType.JSON)
                .body("{\"task\":\"new Task\",\"dueDate\":\"2022-01-01\"}")
                .when()
                    .post("/todo").then().statusCode(201);
    }

    @Test
    public void shouldNotSaveInvalidTask(){
        given()
                .contentType(ContentType.JSON)
                .body("{\"task\":\"new Task\",\"dueDate\":\"2019-01-01\"}")
                .when()
                .post("/todo").then().statusCode(400).body("message", CoreMatchers.is("Due date must not be in past"));
    }

}
