package be.kdg.hackathonsetup.controllerini;

import be.kdg.hackathonsetup.controllerini.dto.GenerateSideQuestDto;
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

    @PostMapping("/generate")
    public ResponseEntity<List<Geopoint>> generateSideQuestJson(
            @RequestBody GenerateSideQuestDto dto) {

        System.out.println("Received sidequest request:");
        System.out.println("- User ID: " + dto.userId());
        System.out.println("- Preference: " + dto.preference());
        System.out.println("- Count: " + dto.count());

        if (dto.count() < 1 || dto.count() > 5) {
            System.out.println("Invalid count: " + dto.count());
            return ResponseEntity.badRequest().build();
        }

        Optional<MonsteriniUser> userOpt = userRepository.findById(dto.userId());
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

        String prompt = geminiService.buildPrompt(dto.preference(), places, questionnaire);
        System.out.println("Generated prompt for Gemini:\n" + prompt);

        String response = geminiService.generate(prompt);
        System.out.println("Gemini raw response:\n" + response);

        // TODO: parse response and return real result
        List<Geopoint> selected;

        try {
            selected = parseGeminiResponse(response, places);
            System.out.println("Returning " + selected.size() + " selected places from Gemini.");
        } catch (Exception e) {
            System.out.println("Failed to parse Gemini response, returning fallback top " + dto.count() + " places.");
            selected = places.subList(0, Math.min(dto.count(), places.size()));
        }


        return ResponseEntity.ok(selected);

    }

    private List<Geopoint> parseGeminiResponse(String response, List<Geopoint> available) {
        List<Geopoint> selected = new ArrayList<>();

        Map<String, Geopoint> coordMap = available.stream()
                .collect(Collectors.toMap(
                        g -> formatCoords(g.getLat(), g.getLon()),
                        g -> g,
                        (a, b) -> a
                ));

        for (String line : response.split("\n")) {
            // Match anything like: "51.123456, 4.123456"
            String coordRegex = "(\\d+\\.\\d+),\\s*(\\d+\\.\\d+)";
            var matcher = java.util.regex.Pattern.compile(coordRegex).matcher(line);

            while (matcher.find()) {
                try {
                    double lat = Double.parseDouble(matcher.group(1));
                    double lon = Double.parseDouble(matcher.group(2));
                    String key = formatCoords(lat, lon);
                    System.out.println("Trying Gemini coord: " + key);
                    if (coordMap.containsKey(key)) {
                        selected.add(coordMap.get(key));
                    } else {
                        System.out.println("No match for: " + key);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Failed to parse coord in line: " + line);
                }
            }
        }

        return selected;
    }

    private String formatCoords(double lat, double lon) {
        return String.format(Locale.US, "%.5f,%.5f", lat, lon);
    }

}
