import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import scala.jdk.CollectionConverters._

import java.util.Properties
import java.time.Duration

object Consumer extends App {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:29092")
  props.put("group.id", "consumer1")
  props.put("max.poll.records", 15)

  val consumer = new KafkaConsumer(props, new StringDeserializer, new StringDeserializer)

  consumer.subscribe(List("books").asJavaCollection)
  var partitions: Map[Long, List[Long]] = Map()
  var data: Map[Long, String] = Map()
  consumer
    .poll(Duration.ofSeconds(1))
    .asScala
    .foreach { r => {
      partitions = partitions.updated(r.partition(), partitions.getOrElse(r.partition(), List.empty[Long]) :+ r.offset())
      data += (r.offset() -> r.value())
    }
    }
  partitions.foreach(x => {
    val offsets = x._2.sorted(Ordering.Long.reverse).take(5)
    offsets.foreach(y => printf("partition: %d, offset: %d, data: %s\n", x._1, y, data.get(y)))
  })
  consumer.close()


}
