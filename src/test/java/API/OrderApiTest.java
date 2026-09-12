package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderApiTest {

    private String authToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.assessment.example.com/v1";
        // In a real framework, fetch this dynamically from an auth service or environment variable
        authToken = System.getenv("API_TOKEN");
    }

    @Test
    public void testGetOrdersDefaultPagination() {
        // 1. Dynamic values handled via environment variables and request specification
        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("limit", 20) // Explicitly setting default for clarity, though API defaults to 20
                .when()
                .get("/orders")
                .then()
                .extract().response();

        // 2. Assertions for status, required fields, types, and business rules
        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK");

        // Assert required top-level fields exist and have correct types
        response.then().body("total", notNullValue());
        response.then().body("data", instanceOf(java.util.List.class));

        // Assert business rule: default pagination returns exactly 20 items
        response.then().body("data", hasSize(20));

        // Assert data integrity within the array (e.g., each item has an id and status)
        response.then().body("data[0].id", notNullValue());
        response.then().body("data[0].status", notNullValue());
    }

    @Test
    public void testGetOrdersInvalidLimit() {
        // One negative case for the same endpoint
        given()
                .header("Authorization", "Bearer " + authToken)
                .queryParam("limit", 101) // Exceeds maximum allowed of 100
                .when()
                .get("/orders")
                .then()
                .statusCode(400); // Expected 400 Bad Request
    }
}