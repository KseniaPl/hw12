import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties
import org.apache.commons.csv.CSVFormat
import java.io.FileReader

import play.api.libs.json._

object Producer extends App {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:29092")

  val producer = new KafkaProducer(props, new StringSerializer, new StringSerializer)
  try {

    val in = new FileReader("/home/kseniya/course/bestsellers with categories.csv")
    import java.io.BufferedReader
    val bufferedReader = new BufferedReader(in)
    bufferedReader.readLine
    val records = CSVFormat.RFC4180.parse(bufferedReader)


    records.forEach(record => {

      val json: JsValue = JsObject(Seq(
        "name" -> JsString(record.get(0)),
        "author" -> JsString(record.get(1)),
        "userRating" -> JsNumber(record.get(2).toDouble),
        "reviews" -> JsNumber(record.get(3).toInt),
        "price" -> JsNumber(record.get(4).toDouble),
        "year" -> JsNumber(record.get(5).toInt),
        "genre" -> JsString(record.get(6))
      ))
      val metadata = producer.send(new ProducerRecord("books", json.toString()))
      println(metadata.get().partition())
    })
  }
  catch {
    case e: Exception => e.printStackTrace()
  }
  finally {
    producer.close()
  }

}
