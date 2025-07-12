flink run /opt/flink/usrlib/Job-1.0-SNAPSHOT.jar

docker exec -it flink-taskmanager /bin/sh
cd /opt/flink/log
ls -l
tail -f flink-*-taskexecutor-*.out

docker exec -it <nombre-del-broker> sh
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic sensores --from-beginning

docker exec -it flink-taskmanager sh 
cd /opt/flink/log 
tail -f flink-*-taskexecutor-*.log

docker inspect prototipo-cassandra | Select-String "IPAddress"