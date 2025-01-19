package com.example.inventory_service;

import com.example.inventory_service.dto.InventoryRequest;
import com.example.inventory_service.dto.InventoryResponse;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.MySQLContainer;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	@BeforeEach
	void setupData() {

		RestAssured.given()
				.post("/api/inventory/add?name=laptop&quantity=100");
	}
	static {
		mySQLContainer.start();
	}

	@Test
	void shouldReadInventory() {
		var response = RestAssured.given()
				.when()
				.get("/api/inventory?name=laptop&quantity=1")
				.then()
				.log().all()
				.statusCode(HttpStatus.OK.value())
				.extract().response().as(Boolean.class);
		assertTrue(response);

		var negativeResponse = RestAssured.given()
				.when()
				.get("/api/inventory?name=laptop&quantity=5000")
				.then()
				.log().all()
				.statusCode(HttpStatus.OK.value())
				.extract().response().as(Boolean.class);
		assertFalse(negativeResponse);

	}
	@Test
	void shouldUpdateQuantity() {

		var response = RestAssured.given()
				.when().put("/api/inventory/update?name=laptop&quantity=10")
				.then().log().all()
				.statusCode(HttpStatus.OK.value())
				.extract().response().asString();
		assertEquals("Quantity of item laptop updated", response);

		var itemResponse = RestAssured.given()
				.when().get("/api/inventory/find?name=laptop")
				.then().log().all()
				.statusCode(HttpStatus.OK.value())
				.extract().response().asString();


		assertTrue(itemResponse.contains("110"));
	}
	@Test
	void shouldAddNewToInventory() {

		var response = RestAssured.given()
				.when()
				.post("/api/inventory/add?name=laptop2&quantity=100")
				.then().log().all()
				.statusCode(HttpStatus.CREATED.value())
				.extract().response().asString();
		assertEquals("Item added to stock", response);


		var negativeResponse = RestAssured.given()
				.when()
				.post("/api/inventory/add?name=laptop&quantity=100")
				.then().log().all()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.extract().response().asString();
		assertEquals("Item laptop is already in stock", negativeResponse);
	}
	@Test
	void shouldFindByName(){
		var response = RestAssured.given()
				.when()
				.get("/api/inventory/find?name=laptop")
				.then().log().all()
				.statusCode(HttpStatus.OK.value())
				.body("size()",Matchers.greaterThan(0))
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.notNullValue())
				.body("quantity",Matchers.notNullValue());


	}
}
