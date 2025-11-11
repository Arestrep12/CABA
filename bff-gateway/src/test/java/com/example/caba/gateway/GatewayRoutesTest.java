package com.example.caba.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import reactor.core.publisher.Mono;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
            "services.core=http://example.org/core",
            "services.agenda=http://example.org/agenda",
            "services.notifications=http://example.org/notifications"
        })
@AutoConfigureWebTestClient
class GatewayRoutesTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void contextLoads() {
        ResponseSpec response = webTestClient.get().uri("/actuator/health").exchange();
        response.expectStatus().isOk();
    }
}

