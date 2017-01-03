First run zookeeper server and Kafka server.

To Train the Model:
	sbt "run <pathname>”


Then,
Run kafka consumer from KafkaConsumer directory using following command
	sbt "run <zookeeper-server-address> <group-name> <topic-name> <num_of_threads=2> 	<folder-name-where-model-is-created>”

Then,
Run kafka producer from KafkaProducer directory using following command
	sbt "run <broker-address> <path-to-training-data-directory> <topic-name>"

Kafka Consumer will print predictions and accuracy, error values on terminal.
