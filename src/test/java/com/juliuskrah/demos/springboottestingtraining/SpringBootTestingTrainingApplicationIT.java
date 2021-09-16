package com.juliuskrah.demos.springboottestingtraining;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.Test;

public class SpringBootTestingTrainingApplicationIT {
    private static Logger log = System.getLogger(SpringBootTestingTrainingApplicationIT.class.getName());

    @Test
    void testGetServicesByClient() throws IOException, InterruptedException {
        var port = System.getProperty("test.server.port");

        HttpClient httpClient = HttpClient.newBuilder().build();
        var uri = String.format("http://localhost:%s/clients/acme/services", port);
        HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .GET()
            .build();
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        log.log(Level.INFO, () -> String.format("Response received %s", response.body()));
        assertThat(response).isNotNull()
            .extracting(HttpResponse::body).isNotNull().asInstanceOf(STRING)
            .contains("\"code\":\"ACME_BOMB\"");
    }
}
