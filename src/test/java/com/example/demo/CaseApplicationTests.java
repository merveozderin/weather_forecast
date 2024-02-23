package com.example.demo;

import com.example.demo.service.WeatherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFluxTest
public class CaseApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void getForecastTest() throws IOException {
        String cityId = "524901";
        String mockedResponseContent = JsonDataLoader.loadJsonMockedResponse("mockedResponse.json");
        Mono<String> mockedResponse = Mono.just(mockedResponseContent);

        // Mock the WeatherService's getForecast method
        Mockito.when(weatherService.getForecast(cityId)).thenReturn(mockedResponse);

        // Test the /forecast endpoint
        webTestClient.get().uri("/forecast?cityId=" + cityId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                // Use consumeWith to work with the response in a reactive way
                .expectBody(String.class).consumeWith(response -> {
                    // Assert that the body is equal to the mockedResponseContent
                    Assertions.assertEquals(mockedResponseContent, response.getResponseBody());
                });
    }
    public static class JsonDataLoader {
        public static String loadJsonMockedResponse(String resourcePath) throws IOException {
            ClassPathResource cpr = new ClassPathResource(resourcePath);
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            return new String(bdata, StandardCharsets.UTF_8);
        }
    }
}

