package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    @Autowired
    private WebClient webClient;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public Mono<String> getForecast(String cityId) {
        return this.webClient.get()
                .uri(uriBuilder -> {
                    URI uri = uriBuilder
                            .queryParam("id", cityId)
                            .queryParam("appid", apiKey)
                            .build();
                    System.out.println("Requesting URL: " + uri);
                    return uri;
                })
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        JsonNode rootNode = objectMapper.readTree(response);
                        JsonNode listNode = rootNode.path("list");
                        ArrayNode filteredList = objectMapper.createArrayNode();

                        /* OpenWeather API'sinden gelen hava durumu tahmini verileri,
                        genellikle 3 saatlik aralıklarla sunulur.
                        Bu nedenle; bir gün için 24 saat / 3 saat = 8 zaman dilimi bulunur.
                        Dolayısıyla; 48 saatlik bir tahmin için ihtiyaç duyulan zaman dilimi sayısı,
                         48 saat / 3 saat = 16 zaman dilimidir. */

                        for (int i = 0; i < 16; i++) {
                            JsonNode item = listNode.get(i);
                            JsonNode mainNode = item.get("main");

                            double maxTemp = mainNode.get("temp_max").asDouble();
                            double feelsLike = mainNode.get("feels_like").asDouble();
                            int humidity = mainNode.get("humidity").asInt();

                            ObjectNode filteredData = objectMapper.createObjectNode();
                            filteredData.put("temp_max", maxTemp);
                            filteredData.put("feels_like", feelsLike);
                            filteredData.put("humidity", humidity);

                            filteredList.add(filteredData);
                        }

                        return filteredList.toString();
                    } catch (Exception e) {
                        return "Error parsing JSON";
                    }
                });
    }
}

