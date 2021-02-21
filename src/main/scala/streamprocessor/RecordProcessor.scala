package streamprocessor

import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json.{JsValue, Json}

import java.time.Instant

object RecordProcessor {
  def process(records: Map[JsValue, JsValue],
              orders: Map[JsValue, JsValue],
              message: ConsumerRecord[Array[Byte], String]): Unit = {
    val json = Json.parse(message.value)
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

  private def processCourier(message: ConsumerRecord[Array[Byte], String]) = {
    val json = Json.parse(message.value)
    //    produceRawMessage(json) -- produce raw message here
    println("matched as courier")
    println((json \ "payload" \ "source" \ "table").as[String])
    println(json \ "payload" \ "after" \ "courier_id")
  }

  private def processOrder(message: ConsumerRecord[Array[Byte], String]) = {
    val json = Json.parse(message.value)
    //    produceRawMessage(json) --produce raw message here
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
