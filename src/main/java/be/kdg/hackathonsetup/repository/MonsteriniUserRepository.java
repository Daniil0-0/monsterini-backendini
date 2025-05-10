package be.kdg.hackathonsetup.repository;

import be.kdg.hackathonsetup.domain.MonsteriniUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonsteriniUserRepository extends JpaRepository<MonsteriniUser, Integer> {
}
