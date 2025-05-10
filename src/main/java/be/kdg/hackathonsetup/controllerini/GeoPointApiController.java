package be.kdg.hackathonsetup.controllerini;

import be.kdg.hackathonsetup.domain.Geopoint;
import be.kdg.hackathonsetup.repository.GeoPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class GeoPointApiController {

    private final GeoPointRepository geoPointRepository;

    @GetMapping("/search")
    public List<Geopoint> searchPlaces(
            @RequestParam(defaultValue = "") String tag1,
            @RequestParam(defaultValue = "") String tag2,
            @RequestParam(defaultValue = "") String tag3,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int validatedSize = Math.min(size, 50);
        return geoPointRepository.findMatchingPlaces(
                tag1.toLowerCase(),
                tag2.toLowerCase(),
                tag3.toLowerCase(),
                PageRequest.of(page, validatedSize)
        );
    }

}
