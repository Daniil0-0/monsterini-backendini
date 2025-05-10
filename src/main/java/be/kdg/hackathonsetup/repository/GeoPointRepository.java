package be.kdg.hackathonsetup.repository;

import be.kdg.hackathonsetup.domain.Geopoint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface GeoPointRepository extends CrudRepository<Geopoint, Long> {

    @Query("""
    SELECT g
    FROM Geopoint g
    WHERE LOWER(g.tags) LIKE %:tag1%
       OR LOWER(g.tags) LIKE %:tag2%
       OR LOWER(g.tags) LIKE %:tag3%
       OR LOWER(g.type) LIKE %:tag1%
       OR LOWER(g.type) LIKE %:tag2%
       OR LOWER(g.type) LIKE %:tag3%
    """)
    List<Geopoint> findMatchingPlaces(
            @Param("tag1") String tag1,
            @Param("tag2") String tag2,
            @Param("tag3") String tag3,
            Pageable pageable
    );

}
