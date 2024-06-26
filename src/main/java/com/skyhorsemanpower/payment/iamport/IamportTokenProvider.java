package com.skyhorsemanpower.payment.iamport;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IamportTokenProvider {

    private static final String IAMPORT_TOKEN_URL = "https://api.iamport.kr/users/getToken";

    @Value("${imp_key}")
    private String KEY;

    @Value("${imp_secret}")
    private String SECRET;

    public String getIamportToken() {
        Map<String, String> body = new HashMap<>();
        body.put("imp_key", KEY);
        body.put("imp_secret", SECRET);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(body);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IAMPORT_TOKEN_URL))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            String responseBody = response.body();
            JsonNode rootNode = mapper.readTree(responseBody);
            JsonNode resNode = rootNode.get("response");
            return resNode.get("access_token").asText();
        } catch (InterruptedException | IOException | RuntimeException e) {
            log.info("getIamportToken error: {}", e.getMessage());
            return null;
        }
    }
}
