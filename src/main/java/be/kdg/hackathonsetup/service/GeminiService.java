package be.kdg.hackathonsetup.service;

import be.kdg.hackathonsetup.domain.Geopoint;
import be.kdg.hackathonsetup.domain.Questionnaire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GeminiService {
    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .build();
    }

    public String buildPrompt(String userInstruction, List<Geopoint> matches, Questionnaire questionnaire) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(questionnaire.enhancePrompt(userInstruction)).append("\n");
        prompt.append("Available places:\n");

        for (Geopoint g : matches) {
            prompt.append("- ").append(g.getType())
                    .append(" at (").append(g.getLat()).append(", ").append(g.getLon()).append(")")
                    .append(" with tags: ").append(g.getTags()).append("\n");
        }

        prompt.append("Pick a few that suit the user preferences for a side quest.");
        return prompt.toString();
    }


    public String generate(String prompt) {
        String url = String.format("/models/gemini-2.0-flash:generateContent?key=%s", apiKey);

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
