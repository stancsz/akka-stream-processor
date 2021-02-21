package streamprocessor

import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json.{JsLookupResult, JsValue, Json}

import java.time.Instant

object RecordProcessor {

  def process(courRecord: Map[JsValue, JsValue],
              orderRecord: Map[JsValue, JsValue],
              message: ConsumerRecord[Array[Byte], String]): Unit = {
    val json = Json.parse(message.value)
    val topic = (json \ "payload" \ "source" \ "table").as[String]
    val event = (json \ "payload" \ "after")
    println(s"${Instant.now().toString()} started processing topic..")
    topic match {
      case "CourierTest" => {
        processCourier(message, courRecord, orderRecord)
      }
      case "OrderTest" => {
        processOrder(message, courRecord, orderRecord)
      }
    }

  }

  /**
   * run matches to order with couriers,
   * if matched, return true, if not return false
   * @param courRecord
   * @param orderRecord
   */
  def matchOrderWithCouriers(event: JsLookupResult, courRecord: Map[JsValue, JsValue]): Unit = {
    println("testing")
    println(event)
    println(courRecord)
  }

  /**
   * run matches to match courier with orders
   * if matched, return true, if not return false
   * @param courRecord
   * @param orderRecord
   */
  def matchCourierWithOrders(event: JsLookupResult, orderRecord: Map[JsValue, JsValue]): Unit = {
    println("testing")
    println(event)
    println(orderRecord)
    // todo: make matches; if matches is made produce message
  }

  private def processCourier(message: ConsumerRecord[Array[Byte], String],
                             courRecord: Map[JsValue, JsValue],
                             orderRecord: Map[JsValue, JsValue] ) = {
    //    produceRawMessage(json) -- produce raw message here
    val meta = Json.parse(message.value)
    /** --- */
    println("matched as courier")
    println((meta \ "payload" \ "source" \ "table").as[String])
    println(meta \ "payload" \ "after" \ "courier_id")
    /** --- */
    val event = (meta \ "payload" \ "after")
    matchCourierWithOrders(event, orderRecord)


  }

  private def processOrder(message: ConsumerRecord[Array[Byte], String],
                           courRecord: Map[JsValue, JsValue],
                           orderRecord: Map[JsValue, JsValue]) = {
//    produceRawMessage(json) --produce raw message here
    val meta = Json.parse(message.value)
    /** --- */
    println("matched as order")
    println((meta \ "payload" \ "source" \ "table").as[String])
    println(meta \ "payload" \ "after" \ "order_id")
    /** --- */



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
