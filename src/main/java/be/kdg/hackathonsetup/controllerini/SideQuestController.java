package be.kdg.hackathonsetup.controllerini;

import be.kdg.hackathonsetup.domain.Geopoint;
import be.kdg.hackathonsetup.domain.MonsteriniUser;
import be.kdg.hackathonsetup.domain.Questionnaire;
import be.kdg.hackathonsetup.repository.GeoPointRepository;
import be.kdg.hackathonsetup.repository.MonsteriniUserRepository;
import be.kdg.hackathonsetup.repository.QuestionnaireRepository;
import be.kdg.hackathonsetup.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sidequest")
@RequiredArgsConstructor
public class SideQuestController {

    private final GeminiService geminiService;
    private final GeoPointRepository geoPointRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final MonsteriniUserRepository userRepository;

    @GetMapping("/generate")
    public ResponseEntity<List<Geopoint>> generateSideQuestJson(
            @RequestParam Long userId,
            @RequestParam String preference,
            @RequestParam int count) {

        System.out.println("Received sidequest request:");
        System.out.println("- User ID: " + userId);
        System.out.println("- Preference: " + preference);
        System.out.println("- Count: " + count);

        if (count < 1 || count > 5) {
            System.out.println("Invalid count: " + count);
            return ResponseEntity.badRequest().build();
        }

        Optional<MonsteriniUser> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty() || userOpt.get().getQuestionnaire() == null) {
            System.out.println("User not found or missing questionnaire.");
            return ResponseEntity.badRequest().build();
        }

        Questionnaire questionnaire = userOpt.get().getQuestionnaire();
        List<String> tags = questionnaire.getPlaceTypes();
        String tag1 = tags.size() > 0 ? tags.get(0).toLowerCase() : "";
        String tag2 = tags.size() > 1 ? tags.get(1).toLowerCase() : "";
        String tag3 = tags.size() > 2 ? tags.get(2).toLowerCase() : "";

        System.out.println("Tags used for place search: " + tag1 + ", " + tag2 + ", " + tag3);

        List<Geopoint> places = geoPointRepository.findMatchingPlaces(tag1, tag2, tag3, PageRequest.of(0, 50));

        System.out.println("Matching places found: " + places.size());

        String prompt = geminiService.buildPrompt(preference, places, questionnaire);
        System.out.println("Generated prompt for Gemini:\n" + prompt);

        String response = geminiService.generate(prompt);
        System.out.println("Gemini raw response:\n" + response);

        // TODO: parse response and return real result
        List<Geopoint> selected;

        try {
            selected = parseGeminiResponse(response, places);
            System.out.println("Returning " + selected.size() + " selected places from Gemini.");
        } catch (Exception e) {
            System.out.println("Failed to parse Gemini response, returning fallback top " + count + " places.");
            selected = places.subList(0, Math.min(count, places.size()));
        }


        return ResponseEntity.ok(selected);

    }

    private List<Geopoint> parseGeminiResponse(String response, List<Geopoint> available) {
        List<Geopoint> selected = new ArrayList<>();

        // Use rounded BigDecimal strings as keys to avoid floating-point mismatch
        Map<String, Geopoint> coordMap = available.stream()
                .collect(Collectors.toMap(
                        g -> formatCoords(g.getLat(), g.getLon()),
                        g -> g,
                        (a, b) -> a
                ));

        for (String line : response.split("\n")) {
            if (line.contains("at (") && line.contains(")")) {
                int start = line.indexOf('(');
                int end = line.indexOf(')', start);
                if (start != -1 && end != -1) {
                    String coordsRaw = line.substring(start + 1, end);
                    String[] parts = coordsRaw.split(",");
                    if (parts.length == 2) {
                        try {
                            double lat = Double.parseDouble(parts[0].trim());
                            double lon = Double.parseDouble(parts[1].trim());
                            String key = formatCoords(lat, lon);
                            System.out.println("Trying Gemini coord: " + key);
                            if (coordMap.containsKey(key)) {
                                selected.add(coordMap.get(key));
                            } else {
                                System.out.println("No match for: " + key);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Failed to parse coords: " + coordsRaw);
                        }
                    }
                }
            }
        }

        return selected;
    }

    private String formatCoords(double lat, double lon) {
        return new java.math.BigDecimal(lat).setScale(6, java.math.RoundingMode.HALF_UP).toPlainString()
                + "," +
                new java.math.BigDecimal(lon).setScale(6, java.math.RoundingMode.HALF_UP).toPlainString();
    }
}
