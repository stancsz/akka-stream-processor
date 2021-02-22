package streamProcessor

import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json.{JsValue, Json}

import java.time.Instant

object RecordProcessor {

  def process(main: ProcessorMain.type,
              message: ConsumerRecord[Array[Byte], String]): Unit = {
    val json = Json.parse(message.value)
    val topic = (json \ "payload" \ "source" \ "table").as[String]
    println(s"${Instant.now().toString()} started processing topic..")
    topic match {
      case "CourierTest" => {
        processCourier(message, main)
      }
      case "OrderTest" => {
        processOrder(message, main)
      }
    }

  }

  private def processCourier(message: ConsumerRecord[Array[Byte], String],
                             main: ProcessorMain.type ) = {
    //    produceRawMessage(json) -- produce raw message here
    val meta = Json.parse(message.value)

    /** --- */
    //    println("matched as courier")
    //    println((meta \ "payload" \ "source" \ "table").as[String])
    //    println(meta \ "payload" \ "after" \ "courier_id")
    /** --- */
    val event = (meta \ "payload" \ "after")
    print(s"line 40...${event}")
    val matched = false

    val courier_id = (event \ "courier_id").toString
    val courier_score = (event \ "courier_score").toString
    val cour_app_created_timestamp = (event \ "app_created_timestamp").toString
    val cour_lat = (event \ "lat").toString
    val cour_lon = (event \ "lon").toString


    def matchRec(record: JsValue): Unit ={
//      courier_id,courier_score,app_created_timestamp,lat,lon
      println("line 49 print rec..")
      val order_id = (record \ "order_id").toString
      val order_score = (record \ "order_score").toString
      val ord_app_created_timestamp = (record \ "app_created_timestamp").toString
      val ord_lat = (record \ "lat").toString
      val ord_lon = (record \ "lon").toString
      println("line 55 print rec..", order_id,order_score,ord_app_created_timestamp,ord_lat,ord_lon)
    }

    main.orderRecords.foreach(
      rec => matchRec(rec._1)
    )
    main.appendOrder(event.get, meta)
  }

  private def processOrder(message: ConsumerRecord[Array[Byte], String],
                           main: ProcessorMain.type) = {
    //    produceRawMessage(json) --produce raw message here
    val meta = Json.parse(message.value)

    /** --- */
    //    println("matched as order")
    //    println((meta \ "payload" \ "source" \ "table").as[String])
    //    println(meta \ "payload" \ "after" \ "order_id")
    /** --- */
    val event = (meta \ "payload" \ "after")
    print(s"line 56...${event}")
    val matched = false
    main.courierRecords.foreach(rec => println(rec._1))
    main.appendOrder(event.get, meta)
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
