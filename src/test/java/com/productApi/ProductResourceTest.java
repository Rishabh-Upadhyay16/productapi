package com.productApi;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class  ProductResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/product")
          .then()
             .statusCode(200);
    }

}