package com.example.product;

import com.example.product.dto.ProductResponse;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	static {
		mongoDBContainer.start();
	}
	@Test
	void shouldCreateProduct() {
		String requestBody = """
				{
				    "name": "APPLE",
				    "description":"An apple",
				    "price": 2
				}
				""";
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name",Matchers.equalTo("APPLE"))
				.body("description",Matchers.equalTo("An apple"))
				.body("price",Matchers.equalTo(2));

	}
	@Test
	void shouldReturnProducts(){
		RestAssured.given()
				.when()
				.get("/api/product")
				.then()
				.statusCode(200)
				.body("size()",Matchers.greaterThan(0))
				.body("[0].id", Matchers.notNullValue())
				.body("[0].name", Matchers.notNullValue())
				.body("description",Matchers.notNullValue())
				.body("price",Matchers.notNullValue());
	}

}
