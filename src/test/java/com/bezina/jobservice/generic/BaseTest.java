package com.bezina.jobservice.generic;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;


@Testcontainers
public abstract class BaseTest {

    private static final int MONGO_PORT = 27017;
    private static final String INIT_JS = "/docker-entrypoint-initdb.d/init.js";
    private static final String MONGO_URI_FORMAT = "mongodb://job_user:job_password@%s:%s/job";

    @Container
    private static final GenericContainer<?> mongo = new GenericContainer(DockerImageName.parse("mongo"))
            .withExposedPorts(MONGO_PORT)
            .withNetworkMode("host")
            .withExtraHost("host-alias", "127.17.0.1")
            .withClasspathResourceMapping("data/job-init.js", INIT_JS, BindMode.READ_ONLY)
            .waitingFor(Wait.forListeningPort())
            .withReuse(true)
            //   .withStartupTimeout(java.time.Duration.ofSeconds(60))
            .withNetwork(Network.SHARED);


    ;


    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) throws InterruptedException {

        mongo.start();
        Thread.sleep(60000);
        registry.add("spring.data.mongodb.uri", () -> String.format(MONGO_URI_FORMAT, mongo.getHost(), mongo.getMappedPort(MONGO_PORT)));
        System.out.println(mongo.getHost()+" "+ mongo.getMappedPort(MONGO_PORT));
    }

}
