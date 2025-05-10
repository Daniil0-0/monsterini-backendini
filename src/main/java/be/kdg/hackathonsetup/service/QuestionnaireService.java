package be.kdg.hackathonsetup.service;

import be.kdg.hackathonsetup.domain.Questionnaire;
import be.kdg.hackathonsetup.repository.QuestionnaireRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;

    public QuestionnaireService(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    public Questionnaire save(Questionnaire questionnaire) {
        return questionnaireRepository.save(questionnaire);
    }

    public Optional<Questionnaire> findByUserId(Long userId) {
        return questionnaireRepository.findByUserId(userId);
    }

    public String buildEnhancedPrompt(Long userId, String userPrompt) {
        Questionnaire prefs = questionnaireRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found for userId: " + userId));
        return prefs.enhancePrompt(userPrompt);
    }
}

