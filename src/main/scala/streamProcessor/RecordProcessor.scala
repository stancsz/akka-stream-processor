package streamProcessor

import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json.{JsValue, Json}

import java.time.Instant

object RecordProcessor {

  def process(processorMain: ProcessorMain,
              message: ConsumerRecord[Array[Byte], String]): Unit = {
    val json = Json.parse(message.value)
    val topic = (json \ "payload" \ "source" \ "table").as[String]
    val event = (json \ "payload" \ "after")
    println(s"${Instant.now().toString()} started processing topic..")
    topic match {
      case "CourierTest" => {
        processCourier(message,)
      }
      case "OrderTest" => {
        processOrder(message,)
      }
    }

  }

  private def processCourier(message: ConsumerRecord[Array[Byte], String],
                             courRecord: Map[JsValue, JsValue],
                             orderRecord: Map[JsValue, JsValue]) = {
    //    produceRawMessage(json) -- produce raw message here
    val meta = Json.parse(message.value)

    /** --- */
    //    println("matched as courier")
    //    println((meta \ "payload" \ "source" \ "table").as[String])
    //    println(meta \ "payload" \ "after" \ "courier_id")
    /** --- */
    val event = (meta \ "payload" \ "after")
    print("line 40...")
    val matched = false
    orderRecord.foreach(rec => println(rec._1 \ "order_id"))
    appendCour()
  }

  private def processOrder(message: ConsumerRecord[Array[Byte], String],
                           courRecord: Map[JsValue, JsValue],
                           orderRecord: Map[JsValue, JsValue]) = {
    //    produceRawMessage(json) --produce raw message here
    val meta = Json.parse(message.value)

    /** --- */
    //    println("matched as order")
    //    println((meta \ "payload" \ "source" \ "table").as[String])
    //    println(meta \ "payload" \ "after" \ "order_id")
    /** --- */
    val event = (meta \ "payload" \ "after")
    print("line 40...")
    val matched = false
    courRecord.foreach(rec => println(rec._1 \ "order_id"))
    orderRecord = orderRecord + (event -> meta)
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
