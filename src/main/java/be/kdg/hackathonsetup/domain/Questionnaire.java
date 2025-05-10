package be.kdg.hackathonsetup.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Questionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ElementCollection
    private List<String> placeTypes;
    private String ageRange;
    private String occupation;
    private String usualPlaces;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private MonsteriniUser user;
    public String enhancePrompt(String userPrompt) {
        StringBuilder sb = new StringBuilder();
        sb.append("User prompt: ").append(userPrompt).append("\n");
        sb.append("Preferences:\n");
        sb.append("- Interested place types: ")
                .append(placeTypes != null && !placeTypes.isEmpty() ? String.join(", ", placeTypes) : "Not specified")
                .append("\n");
        sb.append("- Age range: ").append(ageRange != null ? ageRange : "Not specified").append("\n");
        sb.append("- Occupation: ").append(occupation != null ? occupation : "Not specified").append("\n");
        sb.append("- Usual places: ").append(usualPlaces != null && !usualPlaces.isEmpty() ? usualPlaces : "Not specified").append("\n");
        return sb.toString();
    }
}
