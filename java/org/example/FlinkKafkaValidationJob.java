package org.example;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class FlinkKafkaValidationJob {

    private static final Logger LOG = LoggerFactory.getLogger(FlinkKafkaValidationJob.class);

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "prototipo-kafka:9092");
        properties.setProperty("group.id", "flink-sensores-group5");
        properties.setProperty("auto.offset.reset", "earliest");

        DataStream<String> kafkaStream = env.addSource(new FlinkKafkaConsumer<>(
                "sensores",
                new SimpleStringSchema(),
                properties
        ));

        DataStream<LecturaSensor> sensorStream = kafkaStream.map(new MapFunction<String, LecturaSensor>() {
            @Override
            public LecturaSensor map(String value) throws Exception {
                LOG.info("MENSAJE DESDE KAFKA: {}", value);

                JSONObject json = new JSONObject(value);
                int sensorId = json.getInt("sensor_id");
                double temperatura = json.getDouble("temperatura");
                double humedad = json.getDouble("humedad");
                long timestamp = json.getLong("timestamp");

                return new LecturaSensor(sensorId, temperatura, humedad, timestamp);
            }
        });

        sensorStream.addSink(new CassandraSinkFunction());

        env.execute("Flink Kafka to Cassandra with Logging");
    }

    public static class LecturaSensor {
        public int sensor_id;
        public double temperatura;
        public double humedad;
        public long timestamp;

        public LecturaSensor() {}

        public LecturaSensor(int sensor_id, double temperatura, double humedad, long timestamp) {
            this.sensor_id = sensor_id;
            this.temperatura = temperatura;
            this.humedad = humedad;
            this.timestamp = timestamp;
        }
    }
}
