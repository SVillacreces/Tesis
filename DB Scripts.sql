CREATE TABLE sensores_ks.lecturas_sensores (
    sensor_id int,
    timestamp bigint,
    temperatura double,
    humedad double,
    PRIMARY KEY (sensor_id, timestamp)
);

select *
from lecturas_sensores;