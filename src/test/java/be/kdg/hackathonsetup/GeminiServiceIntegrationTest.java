package be.kdg.hackathonsetup;

import be.kdg.hackathonsetup.service.GeminiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeminiServiceIntegrationTest {

    @Autowired
    private GeminiService geminiService;

    @Test
    void shouldGenerateContentFromPrompt() {
        String response = geminiService.generate("What is the capital of Belgium?");
        System.out.println("Gemini response: " + response);
        assert response.toLowerCase().contains("brussels");
    }
}
