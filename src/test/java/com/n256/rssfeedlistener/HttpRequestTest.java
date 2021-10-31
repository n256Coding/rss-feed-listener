package com.n256.rssfeedlistener;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldRespondWithHTTPStatusOK() {
        ResponseEntity<JsonNode> entity = restTemplate.getForEntity("http://localhost:" + port + "/items", JsonNode.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnTwoResults() throws InterruptedException {

        // Waiting for scheduler to update feeds into database
        Thread.sleep(10000);

        ResponseEntity<JsonNode> entity = restTemplate.getForEntity("http://localhost:" + port + "/items?page=1&size=2&sort=updated_date&direction=desc", JsonNode.class);

        assertThat(entity.getBody().size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnTenResults() throws InterruptedException {

        // Waiting for scheduler to update feeds into database
        Thread.sleep(10000);

        ResponseEntity<JsonNode> entity = restTemplate.getForEntity("http://localhost:" + port + "/items", JsonNode.class);

        assertThat(entity.getBody().size()).isEqualTo(10);
    }
}
