import java.util.Properties
import java.lang._
import kafka.producer._
import java.io.File
/**
 * @author Group No. 4
 */
object KafkaProducer {
  
  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.println("Usage: KafkaProducer <metadataBrokerList> <path_to_real_data> <topic>")
      System.exit(1)
    }

    val Array(brokers, datapath, topic) = args
    // Zookeper connection properties
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")

    val config = new ProducerConfig(props)
    val producer = new Producer[String, String](config)

    // Send some messages
    //var fs:Array[File]=new java.io.File(datapath).listFiles.filter(x => !x.getName.contains("DS_Store")).flatMap { x => x.listFiles() }
    //val messages = fs.flatMap { x => scala.io.Source.fromFile(x.getAbsolutePath).getLines() }
    val messages = scala.io.Source.fromFile(datapath).getLines()
    messages.foreach { x => producer.send(new KeyedMessage(topic,x)) }
  }


}
