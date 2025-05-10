package be.kdg.hackathonsetup.repository;

import com.azure.core.models.GeoPoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoPointRepository extends CrudRepository<GeoPoint, Long> {
}
