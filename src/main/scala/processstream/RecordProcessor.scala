package processstream
import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json.Json

object RecordProcessor {
  def process(records: Map[String, ConsumerRecord[Array[Byte], String]], message: ConsumerRecord[Array[Byte], String]): Unit = {
    processPrintHelper(records, message)
    val json = Json.parse(message.value)
    println(json \ "payload" \ "after" \ "order_id")
    println(json \ "payload" \ "source" \ "table")
  }

  private def processPrintHelper(records: Map[String, ConsumerRecord[Array[Byte], String]], message: ConsumerRecord[Array[Byte], String]) = {
    println(s"Message Received : ${message.timestamp} - ${message.value}")
    println(s"Value: ${message.value.getClass}")
    val json = Json.parse(message.value)
    records.foreach {
      println
    }
  }
}
