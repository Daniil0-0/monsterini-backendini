package be.kdg.hackathonsetup.repository;

import be.kdg.hackathonsetup.domain.MonsteriniUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MonsteriniUserRepository extends JpaRepository<MonsteriniUser, Long> {
    Optional<MonsteriniUser> findByEmail(String email);

    @Query("SELECT u FROM MonsteriniUser u ORDER BY u.xp DESC")
    List<MonsteriniUser> findTopUsers(Pageable pageable);
}
