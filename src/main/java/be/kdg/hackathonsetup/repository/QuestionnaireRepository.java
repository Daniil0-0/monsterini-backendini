package be.kdg.hackathonsetup.repository;

import be.kdg.hackathonsetup.domain.MonsteriniUser;
import be.kdg.hackathonsetup.domain.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Integer> {

    Optional<Questionnaire> findByUserId(Long userId);
}
