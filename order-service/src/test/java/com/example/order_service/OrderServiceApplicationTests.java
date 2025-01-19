package com.example.order_service;

import com.example.order_service.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@Testcontainers
class OrderServiceApplicationTests {

	@Container
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3.0")
			.withDatabaseName("testdb")
			.withUsername("user")
			.withPassword("password");

	@LocalServerPort
	private Integer port;

	@BeforeAll
	static void setupContainer() {
		mySQLContainer.start();
		System.setProperty("MYSQL_PORT", String.valueOf(mySQLContainer.getMappedPort(3306)));
	}

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
            {
                "orderNumber": "ORD12345",
                "name": "John Doe",
                "products": [
                    {
                        "name": "Laptop",
                        "price": 1200.00,
                        "quantity": 1
                    },
                    {
                        "name": "Mouse",
                        "price": 100.00,
                        "quantity": 1
                    }
                ]
            }
            """;


		stubFor(get(urlEqualTo("/api/inventory?name=Laptop&quantity=1"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("true")
						.withHeader("Content-Type", "application/json")));

		stubFor(get(urlEqualTo("/api/inventory?name=Mouse&quantity=1"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("true")
						.withHeader("Content-Type", "application/json")));

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("Order Placed Successfully"));
	}

	@Test
	void shouldFailWhenProductIsOutOfStock() {
		String submitOrderJson = """
				{
				    "orderNumber": "ORD67890",
				    "name": "Jane Doe",
				    "products": [
				        {
				            "name": "Laptop",
				            "price": 1200.00,
				            "quantity": 1
				        },
				        {
				            "name": "Mouse",
				            "price": 100.00,
				            "quantity": 1
				        }
				    ]
				}
				""";


		stubFor(get(urlEqualTo("/api/inventory?name=Laptop&quantity=1"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("true")
						.withHeader("Content-Type", "application/json")));


		stubFor(get(urlEqualTo("/api/inventory?name=Mouse&quantity=11"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("false")
						.withHeader("Content-Type", "application/json")));


		try{ RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(500);
	}catch(RuntimeException e ){
			assertThat(e.getMessage(), Matchers.containsString("Product named Mouse isn't in stock"));
		}


	}
}
