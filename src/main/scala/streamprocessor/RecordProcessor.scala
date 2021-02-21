package streamprocessor
import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json.Json
import streamprocessor.MessageProducer.produceRawMessage

import java.time.Instant

object RecordProcessor {
  def process(records: Map[String, ConsumerRecord[Array[Byte], String]],
//              records: Map[String, ConsumerRecord[Array[Byte], String]],
              message: ConsumerRecord[Array[Byte], String]): Unit = {
//    processPrintHelper(records, message)
//    val json = Json.parse(message.value)
    println(s"${Instant.now().toString()} started processing topic..")
    processTopic(message)
  }

  private def processTopic(message: ConsumerRecord[Array[Byte], String]) = {
    val json = Json.parse(message.value)
    val topic = (json \ "payload" \ "source" \ "table").as[String]
    topic match {
      case "CourierTest" => processCourier(message)
      case "OrderTest" => processOrder(message)
    }
  }

  private def processCourier(message: ConsumerRecord[Array[Byte], String])={
    val json = Json.parse(message.value)
    produceRawMessage(json)
    println("matched as courier")
    println((json \ "payload" \ "source" \ "table").as[String])
    println(json \ "payload" \ "after" \ "courier_id")
  }

  private def processOrder(message: ConsumerRecord[Array[Byte], String])={
    val json = Json.parse(message.value)
    produceRawMessage(json)
    println("matched as order")
    println((json \ "payload" \ "source" \ "table").as[String])
    println(json \ "payload" \ "after" \ "order_id")
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
