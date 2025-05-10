package be.kdg.hackathonsetup.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class MonsteriniUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String userName;
    private String password;
    private int xp;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Questionnaire questionnaire;
}
