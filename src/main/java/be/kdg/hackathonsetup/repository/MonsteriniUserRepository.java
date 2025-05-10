package be.kdg.hackathonsetup.repository;

import be.kdg.hackathonsetup.domain.MonsteriniUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonsteriniUserRepository extends JpaRepository<MonsteriniUser, Long> {
    Optional<MonsteriniUser> findByEmail(String email);

}
