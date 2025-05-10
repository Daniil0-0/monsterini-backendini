package be.kdg.hackathonsetup;

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
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void run(String... args) throws Exception {
//        List<GeoNode> records = readGeoNodes();
//        records.forEach(System.out::println);
    }


    private List<GeoNode> readGeoNodes() throws Exception {
        URI resourceUri = getClass().getClassLoader().getResource("data/mydata.parquet").toURI();
        java.nio.file.Path tempFile = Files.createTempFile("nodes-temp-", ".parquet");
        Files.copy(java.nio.file.Paths.get(resourceUri), tempFile, StandardCopyOption.REPLACE_EXISTING);

        List<GeoNode> results = new ArrayList<>();
        Path path = new Path(tempFile.toString());
        Configuration conf = new Configuration();

        try (ParquetReader<GenericRecord> reader =
                     AvroParquetReader.<GenericRecord>builder(path).withConf(conf).build()) {

            GenericRecord record;
            while ((record = reader.read()) != null) {
                Object idObj = record.get("id");
                Object typeObj = record.get("type");
                Object latObj = record.get("lat");
                Object lonObj = record.get("lon");

                if (idObj == null || typeObj == null || latObj == null || lonObj == null) {
                    continue;
                }

                Object tagsObj = record.get("tags");
                Map<String, String> tags = new HashMap<>();

                if (tagsObj instanceof CharSequence str) {
                    try {
                        tags = objectMapper.readValue(str.toString(), Map.class);
                    } catch (Exception e) {
                        System.err.println("Failed to parse tags JSON: " + str);
                    }
                }

                // we don't care about the nodes with empty desc
                if (tags.isEmpty()) {
                    continue;
                }

                GeoNode node = new GeoNode();
                node.setId(Long.parseLong(idObj.toString()));
                node.setType(typeObj.toString());
                node.setLat(Double.parseDouble(latObj.toString()));
                node.setLon(Double.parseDouble(lonObj.toString()));
                node.setTags(tags);

                results.add(node);
            }
        }

        return results;
    }


}
