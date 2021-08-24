import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import scala.jdk.CollectionConverters._

import java.util.Properties
import java.time.Duration

object Consumer extends App {
  val maxMsgForPartition = 5
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:29092")
  props.put("group.id", "consumer1")
  props.put("max.poll.records", 15)

  val consumer = new KafkaConsumer(props, new StringDeserializer, new StringDeserializer)

  consumer.subscribe(List("books").asJavaCollection)
  var partitions: Map[Long, List[Long]] = Map()
  var data: Map[Long, String] = Map()
  consumer.poll(100)
  val asp = consumer.assignment()
  consumer.seekToEnd(asp)
  asp.forEach {
    tp =>
      consumer.seek(tp, consumer.position(tp) - maxMsgForPartition)
      consumer
        .poll(Duration.ofSeconds(1))
        .asScala
        .foreach { r => {
          printf("partition: %d, offset: %d, data: %s\n", tp.partition(), r.offset(), r.value())
        }
        }
  }
  consumer.close()


}
