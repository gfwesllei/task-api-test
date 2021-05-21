package br.com.devwell.tasks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

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

    @Test
    public void shouldNotDeleteTask(){
        Long idDelete =0L;
        given()
                .pathParam("id",idDelete)
                .when()
                .delete("/todo/{id}").then().statusCode(400).body("message", CoreMatchers.containsString("Task not found"));
    }

    @Test
    public void shouldRemoveTask(){
        Integer idResponse = given()
                .contentType(ContentType.JSON)
                .body("{\"task\":\"new Task Remove\",\"dueDate\":\"2030-01-01\"}")
                .when()
                .post("/todo").then().statusCode(201).extract().response().path("id");

        given().pathParam("id",idResponse).when().delete("/todo/{id}").then().statusCode(200);
    }

}
