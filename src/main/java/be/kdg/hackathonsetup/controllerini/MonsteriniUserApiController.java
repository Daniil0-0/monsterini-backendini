package be.kdg.hackathonsetup.controllerini;

import be.kdg.hackathonsetup.controllerini.dto.RegisterUserDto;
import be.kdg.hackathonsetup.domain.Geopoint;
import be.kdg.hackathonsetup.domain.MonsteriniUser;
import be.kdg.hackathonsetup.domain.Questionnaire;
import be.kdg.hackathonsetup.repository.GeoPointRepository;
import be.kdg.hackathonsetup.repository.MonsteriniUserRepository;
import be.kdg.hackathonsetup.repository.QuestionnaireRepository;
import be.kdg.hackathonsetup.service.GeminiService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MonsteriniUserApiController {
    private final GeminiService geminiService;
    private final QuestionnaireRepository questionnaireRepository;
    private final GeoPointRepository geopointRepository;
    private final MonsteriniUserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<MonsteriniUser> getUser(@PathVariable Long id) {
        Optional<MonsteriniUser> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MonsteriniUser> createUser(@RequestBody MonsteriniUser user) {
        MonsteriniUser savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonsteriniUser> updateUser(@PathVariable Long id, @RequestBody MonsteriniUser updatedUser) {
        MonsteriniUser user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setEmail(updatedUser.getEmail());
        user.setUserName(updatedUser.getUserName());
        user.setPassword(updatedUser.getPassword());
        user.setXp(updatedUser.getXp());
        // TODO: update home address when implemented

        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<MonsteriniUser> register(@RequestBody RegisterUserDto dto) {

        MonsteriniUser user = new MonsteriniUser();
        user.setId(null);
        user.setEmail(dto.email());
        user.setUserName(dto.userName());
        user.setPassword(dto.password());
        user.setXp(0);

        MonsteriniUser saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/sidequest/{userId}")
    public String generateSideQuest(@PathVariable Long userId,
                                    @RequestParam(defaultValue = "Give me a fun side quest!") String instruction) {
        Questionnaire questionnaire = questionnaireRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No questionnaire found"));

        List<String> types = questionnaire.getPlaceTypes();
        String tag1 = types.size() > 0 ? types.get(0).toLowerCase() : "";
        String tag2 = types.size() > 1 ? types.get(1).toLowerCase() : "";
        String tag3 = types.size() > 2 ? types.get(2).toLowerCase() : "";

        List<Geopoint> matches = geopointRepository.findMatchingPlaces(tag1, tag2, tag3, PageRequest.of(0, 20));

        String prompt = geminiService.buildPrompt(instruction, matches, questionnaire);
        return geminiService.generate(prompt);
    }

    @PostMapping("/login")
    public ResponseEntity<MonsteriniUser> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<MonsteriniUser> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        MonsteriniUser user = optionalUser.get();
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        return ResponseEntity.ok(user);
    }
}