package be.kdg.hackathonsetup.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeminiService {
    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://generativelanguage.googleapis.com/gemini-2.0-flash-lite")
                .build();
    }

    public String generate(String prompt) {
        String url = String.format("/gemini-pro:generateContent?key=%s", apiKey);

        String requestBody = """
            {
              "contents": [
                {
                  "parts": [
                    {
                      "text": "%s"
                    }
                  ]
                }
              ]
            }
            """.formatted(prompt);

        return webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()))
                .block();
    }
}
