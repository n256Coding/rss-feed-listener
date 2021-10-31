package com.n256.rssfeedlistener;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RSSPollingTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void schedulerShouldRunAndRetrieveData() throws InterruptedException {

//        Wait till scheduler executes
        Thread.sleep(10000);

        ResponseEntity<JsonNode> entity = restTemplate.getForEntity("http://localhost:" + port + "/items", JsonNode.class);

        assertThat(entity.getBody().size()).isGreaterThan(0);
    }
}
