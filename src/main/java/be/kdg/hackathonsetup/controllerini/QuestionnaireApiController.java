package be.kdg.hackathonsetup.controllerini;

import be.kdg.hackathonsetup.domain.Questionnaire;
import be.kdg.hackathonsetup.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/questionnaire")
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionnaireApiController {

    private final QuestionnaireRepository questionnaireRepository;

    @Autowired
    public QuestionnaireApiController(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    @PostMapping
    public ResponseEntity<Questionnaire> createOrUpdateQuestionnaire(@RequestBody Questionnaire questionnaire) {
        Optional<Questionnaire> existing = questionnaireRepository.findByUserId(questionnaire.getUser().getId());
        if (existing.isPresent()) {
            Questionnaire toUpdate = existing.get();
            toUpdate.setPlaceTypes(questionnaire.getPlaceTypes());
            toUpdate.setAgeRange(questionnaire.getAgeRange());
            toUpdate.setOccupation(questionnaire.getOccupation());
            toUpdate.setUsualPlaces(questionnaire.getUsualPlaces());
            // ...any other fields
            Questionnaire saved = questionnaireRepository.save(toUpdate);
            return ResponseEntity.ok(saved);
        } else {
            Questionnaire saved = questionnaireRepository.save(questionnaire);
            return ResponseEntity.ok(saved);
        }
    }
}
