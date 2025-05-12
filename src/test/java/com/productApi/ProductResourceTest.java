package com.productApi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class ProductResourceTest {

    @Test
    public void testAddNewProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(99.99);
        product.setQuantity(10);
        product.setDescription("Test description");

        given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/product")
                .then()
                .statusCode(201)
                .header("Location", containsString("/product/"));
    }

    @Test
    public void testGetAllProducts() {
        given()
                .when()
                .get("/product")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testGetProductById_NotFound() {
        given()
                .when()
                .get("/product/999999") // Assuming this ID doesn't exist
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetStockAvailable() {
        int productId = 1; // Replace with a valid ID from your DB
        given()
                .queryParam("count", 5)
                .when()
                .get("/product/" + productId + "/stock")
                .then()
                .statusCode(anyOf(is(200), is(404))); // 404 if not found
    }

    @Test
    public void testSortedProductList() {
        given()
                .when()
                .get("/product/SortedProductList")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testUpdateProduct_NotFound() {
        Product updated = new Product();
        updated.setName("Updated Product");
        updated.setPrice(10.0);
        updated.setQuantity(5);

        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put("/product/999999")
                .then()
                .statusCode(400);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        given()
                .when()
                .delete("/product/999999")
                .then()
                .statusCode(400);
    }
}
