package be.kdg.hackathonsetup;

import lombok.Data;

import java.util.Map;

@Data
public class GeoNode {
    private long id;
    private String type;
    private double lat;
    private double lon;
    // TODO: add tags
    //
    private Map<String, String> tags;
}
