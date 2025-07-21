// File: src/main/java/nl/nijhuissven/database/Database.java
package nl.nijhuissven.galaxor.database;

import nl.nijhuissven.galaxor.Galaxor;
import nl.nijhuissven.galaxor.configuration.MainConfiguration;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.write.Point;
import com.influxdb.client.domain.WritePrecision;

import java.time.Instant;
import java.util.Map;

public class Database {
    private final InfluxDBClient influxDB;
    private final String measurement;

    public Database() {
        MainConfiguration config = Galaxor.instance().configuration();
        String url = config.influxUrl();
        String org = config.influxOrg();
        String bucket = config.influxBucket();
        String token = config.influxToken();
        this.measurement = config.influxMeasurement();
        this.influxDB = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }

    public void writeEvent(String measurement, Map<String, Object> fields, Map<String, String> tags) {
        Point point = Point.measurement(measurement)
                .addFields(fields)
                .addTags(tags)
                .time(Instant.now(), WritePrecision.MS);
        influxDB.getWriteApiBlocking().writePoint(point);
    }

    public InfluxDBClient getClient() {
        return influxDB;
    }

    public void disconnect() {
        influxDB.close();
    }
}