package com.bezina.integrationT;

import com.bezina.integrationT.generic.BaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
class JobServiceAppIT extends BaseTest {

	@Autowired
	private WebTestClient client;

	@Autowired
	private Environment env;

	@Test
	public void testMongoUri() {
		// URI MongoDB from ApplicationContext
		String configuredUri = env.getProperty("spring.data.mongodb.uri");

		System.out.println("Configured Mongo URI: " + configuredUri);
		Assertions.assertThat(configuredUri).isEqualTo(mongoUri);
	}

	@Test
	void AllJobTest() {

			this.client.get()
					.uri("job/all")
					.exchange()
					.expectStatus().is2xxSuccessful()
					.expectBody()
					.jsonPath("$").isNotEmpty();
		}

}
