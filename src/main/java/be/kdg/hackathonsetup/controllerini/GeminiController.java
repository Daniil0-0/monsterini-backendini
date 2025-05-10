package be.kdg.hackathonsetup.controllerini;

import be.kdg.hackathonsetup.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController {
    private final GeminiService geminiService;

    @GetMapping("/test")
    public ResponseEntity<String> testGemini() {
        String result = geminiService.generate("Tell me a joke.");
        return ResponseEntity.ok(result);
    }

    // LOGIC FOR GIVING A SIDE QUEST

    // here we get 2 things from the frontend:
    // an id of the user and a number of places the user
    // would want to visit
    // and then we call query and gemini logic
}
