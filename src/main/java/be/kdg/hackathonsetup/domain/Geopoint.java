package be.kdg.hackathonsetup.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
public class Geopoint {

    @Id
    private Long id;
    private String type;

    // Store tags as a JSON string; you can later use @Convert or a JSON library for mapping if desired
    private String tags;

    private Double lat;
    private Double lon;

    public Geopoint() {
    }

    public Geopoint(Long id, String type, String tags, Double lat, Double lon) {
        this.id = id;
        this.type = type;
        this.tags = tags;
        this.lat = lat;
        this.lon = lon;
    }
}
