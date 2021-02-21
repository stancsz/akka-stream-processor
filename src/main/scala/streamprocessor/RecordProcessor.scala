package streamprocessor
import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json.{JsValue, Json}

import java.time.Instant

object RecordProcessor {
  def process(records: Map[String, ConsumerRecord[Array[Byte], String]],
//              records: Map[String, ConsumerRecord[Array[Byte], String]],
              message: ConsumerRecord[Array[Byte], String]): Unit = {
//    processPrintHelper(records, message)
    val json = Json.parse(message.value)
    println(s"${Instant.now().toString()} started processing topic..")
    processTopic(json)

  }

  private def processTopic(json: JsValue) = {
    val topic = (json \ "payload" \ "source" \ "table").as[String]
    topic match {
      case "CourierTest" => processCourier(json)
      case "OrderTest" => processOrder(json)
    }
  }

  /**
   * 
   * @param json
   */
  private def processCourier(json: JsValue)={
    produceRawMessage()
    println("matched as courier")
    println((json \ "payload" \ "source" \ "table").as[String])
    println(json \ "payload" \ "after" \ "courier_id")
  }

  private def processOrder(json: JsValue)={
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
