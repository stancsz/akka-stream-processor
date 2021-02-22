package streamProcessor

import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json.Json

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
    main.orderRecords.foreach(rec => println(rec._1))
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
