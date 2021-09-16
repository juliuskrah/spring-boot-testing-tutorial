package com.juliuskrah.demos.springboottestingtraining;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.hamcrest.Matchers.hasItems;

import java.util.List;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.dto.ClientWithServices;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Julius Krah
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringBootTestingTrainingApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort int port;

	@Test
	void testGetAllClients() {
		var entity = RequestEntity.get("/clients/").build();
		ResponseEntity<List<ClientWithServices>> body = restTemplate.exchange(entity,
			new ParameterizedTypeReference<List<ClientWithServices>>(){});

		assertThat(body).isNotNull()
			.extracting(ResponseEntity::getBody, as(LIST))
			.anySatisfy(requirements 
			-> 	assertThat(requirements).hasFieldOrPropertyWithValue("name", "acme corporation"));
	}

	@Test
	void testAllServicesForClient() {
		var uuid = UUID.fromString("ce74d8f2-ef49-4f2a-b5cc-52ef30046d40");
		given().
			pathParams("id", uuid).and().port(port).
		when().
			get("/clients/{id}").
		then().
			assertThat().statusCode(200).and().
			body("services.findAll { it.queueName == 'queue_acme_catapult' }.code", hasItems("ACME_CATAPULT"));
	}

}
