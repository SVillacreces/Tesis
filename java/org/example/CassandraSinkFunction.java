package org.example;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.net.InetSocketAddress;

public class CassandraSinkFunction extends RichSinkFunction<FlinkKafkaValidationJob.LecturaSensor> {

    private transient CqlSession session;
    private transient PreparedStatement prepared;

    @Override
    public void open(Configuration parameters) throws Exception {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("prototipo-cassandra", 9042))  // Cambia a IP si ejecutas en Docker sin red bridge
                .withLocalDatacenter("datacenter1")
                .withKeyspace("sensores_ks")
                .build();

        prepared = session.prepare("INSERT INTO lecturas_sensores (sensor_id, timestamp, temperatura, humedad) VALUES (?, ?, ?, ?)");
    }

    @Override
    public void invoke(FlinkKafkaValidationJob.LecturaSensor value, Context context) throws Exception {
        BoundStatement bound = prepared.bind(value.sensor_id, value.timestamp, value.temperatura, value.humedad);
        session.execute(bound);
    }

    @Override
    public void close() throws Exception {
        if (session != null) session.close();
    }
}
