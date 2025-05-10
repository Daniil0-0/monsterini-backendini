package be.kdg.hackathonsetup;

import be.kdg.hackathonsetup.domain.Geopoint;
import be.kdg.hackathonsetup.repository.GeoPointRepository;
import com.azure.core.models.GeoPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParquetReaderRunner implements CommandLineRunner {
    private final GeoPointRepository geoPointRepository;

    public ParquetReaderRunner(GeoPointRepository geoPointRepository) {
        this.geoPointRepository = geoPointRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Geopoint> records = readGeopoints();
        if (!records.isEmpty()) {
            geoPointRepository.saveAll(records);
            System.out.println("Imported " + records.size() + " Geopoints from Parquet.");
        } else {
            System.out.println("No Geopoints found to import.");
        }
    }

    private List<Geopoint> readGeopoints() throws Exception {
        URI resourceUri = getClass().getClassLoader().getResource("data/cleaned_train.parquet").toURI();
        java.nio.file.Path tempFile = Files.createTempFile("geopoints-temp-", ".parquet");
        Files.copy(java.nio.file.Paths.get(resourceUri), tempFile, StandardCopyOption.REPLACE_EXISTING);

        List<Geopoint> results = new ArrayList<>();
        Path path = new Path(tempFile.toString());
        Configuration conf = new Configuration();

        try (ParquetReader<GenericRecord> reader =
                     AvroParquetReader.<GenericRecord>builder(path).withConf(conf).build()) {

            GenericRecord record;
            while ((record = reader.read()) != null) {
                Object idObj = record.get("id");
                Object typeObj = record.get("type");
                Object tagsObj = record.get("tags");
                Object latObj = record.get("lat");
                Object lonObj = record.get("lon");

                if (idObj == null || typeObj == null || tagsObj == null || latObj == null || lonObj == null) {
                    continue;
                }

                String tagsStr = tagsObj.toString().trim();
                // Skip nodes with empty tags (either '{}' or empty string)
                if (tagsStr.equals("{}") || tagsStr.isEmpty()) {
                    continue;
                }

                Geopoint geopoint = new Geopoint();
                geopoint.setId(Long.parseLong(idObj.toString()));
                geopoint.setType(typeObj.toString());
                geopoint.setTags(tagsStr);
                geopoint.setLat(Double.parseDouble(latObj.toString()));
                geopoint.setLon(Double.parseDouble(lonObj.toString()));

                results.add(geopoint);
            }
        }

        return results;
    }

}
