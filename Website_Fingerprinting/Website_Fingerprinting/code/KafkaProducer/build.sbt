name := "Kafka Producer"
version := "0.1.0"

val sparkVersion = "1.4.1"

libraryDependencies ++= Seq(
"org.apache.kafka" % "kafka_2.10" % "0.8.2.1",
"org.apache.kafka" % "kafka-clients" % "0.8.2.1",
"org.slf4j" % "slf4j-api" % "1.7.12",
"org.apache.spark" % "spark-mllib_2.10" % "1.4.1",
"org.apache.spark" % "spark-core_2.10" % "1.4.1",
"org.apache.spark" % "spark-streaming_2.10" % "1.4.1",
"org.apache.spark" % "spark-streaming-kafka_2.10" % "1.4.1",
"com.codahale.metrics" % "metrics-core" % "3.0.2",
"log4j" % "log4j" % "1.2.17",
"commons-cli" % "commons-cli" % "1.3.1"
)
