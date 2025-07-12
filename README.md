# Prototipo de Validación de Datos en Tiempo Real

Este repositorio contiene el código fuente y los recursos necesarios para desplegar un **prototipo de validación de datos en tiempo real** utilizando Apache Flink, Apache Kafka y Apache Cassandra.

## 🔹 Tecnologías Utilizadas

- **Apache Kafka**: Ingesta de datos en tiempo real.
- **Apache Flink**: Procesamiento y validación de datos en streaming.
- **Apache Cassandra**: Almacenamiento escalable y distribuido.
- **Docker y Docker Compose**: Orquestación y despliegue local.

---

## 🔹 Estructura del Proyecto

```
├── docker-compose.yml                # Definición de los servicios Kafka, Zookeeper, Cassandra, Flink
├── DB Scripts.sql                     # Script de creación de keyspace y tabla en Cassandra
├── docker commands.sh                 # Comandos útiles para levantar servicios
├── pom.xml                            # Proyecto Maven para Flink
└── java/org/example/
    ├── CassandraSinkFunction.java      # Lógica para insertar datos en Cassandra
    └── FlinkKafkaValidationJob.java    # Job principal de Flink para consumir y procesar datos
```

---

## 📅 Descripción del Flujo de Datos

1. Los datos se generan y envían a **Kafka**.
2. **Flink** consume estos datos desde Kafka, los valida y los transforma.
3. Los datos validados se almacenan en la base de datos **Cassandra**.

El sistema está diseñado para ser escalable y procesar flujos de datos en tiempo real.

---

## 📆 Requisitos

- Docker
- Java 11+
- Maven

---

## 🔧 Despliegue Paso a Paso

1. Clonar el repositorio.
2. Levantar los servicios con:

```bash
docker-compose up -d
```

3. Ejecutar el script **DB Scripts.sql** para crear la tabla en Cassandra.
4. Compilar el job de Flink:

```bash
mvn clean package
```

5. Subir y ejecutar el job en Flink:

```bash
flink run -c org.example.FlinkKafkaValidationJob target/Job-1.0-SNAPSHOT.jar
```

6. Verificar los datos insertados en Cassandra.

---

## 🔎 Notas Adicionales

- Este prototipo puede adaptarse para enviar los datos validados a un segundo topic Kafka, una API REST o cualquier sistema de almacenamiento alternativo.
- Los comandos más comunes están documentados en el archivo `docker commands.sh`.

---

## 📄 Anexo: Código Fuente

### `FlinkKafkaValidationJob.java`

```java
public class FlinkKafkaValidationJob {
    // Código principal para la ingesta y validación en Flink
}
```

### `CassandraSinkFunction.java`

```java
public class CassandraSinkFunction extends RichSinkFunction<LecturaSensor> {
    // Código para insertar los datos validados en Cassandra
}
```

### `pom.xml`

```xml
<!-- Archivo de configuración Maven para empaquetar el Job -->
```

### `docker-compose.yml`

```yaml
# Definición de los contenedores Kafka, Zookeeper, Cassandra y Flink
```

### `DB Scripts.sql`

```sql
CREATE KEYSPACE sensores_ks WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE sensores_ks.lecturas_sensores (
    sensor_id int,
    timestamp bigint,
    temperatura double,
    humedad double,
    PRIMARY KEY (sensor_id, timestamp)
);
```

---

## 📂 Licencia

Uso académico. Repositorio privado. Solicitar acceso al autor.

👉 **Autor:** Sebastian Villacreces
👉 **Proyecto:** Trabajo Fin de Máster - Validación de Datos en Tiempo Real
