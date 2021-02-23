package streamProcessor

import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json.{JsValue, Json}

import java.time.Instant
import scala.math.abs

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
    val event = (meta \ "payload" \ "after")
    print(s"line 40...${event}")

    val courier_id = (event \ "courier_id").get.as[String]
    val courier_score = (event \ "courier_score").get.as[String].toDouble
    val cour_app_created_timestamp = (event \ "app_created_timestamp").get.as[String]
    val cour_lat = (event \ "lat").get.as[String].toDouble
    val cour_lon = (event \ "lon").get.as[String].toDouble


    /**
     * The approximate conversions are: Latitude: 1 deg = 110.574 km. Longitude: 1 deg = 111.320*cos(latitude) km.
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     */
    def distanceCheck(lat1:Double, lon1:Double, lat2:Double, lon2:Double): Boolean ={
      val deltaLat = math.toRadians(abs(lat1 - lat2))
      val deltaLong = math.toRadians(abs(lon1 - lon2))
      val a = math.pow(math.sin(deltaLat / 2), 2) + math.cos(math.toRadians(lat1)) * math.cos(math.toRadians(lat2)) * math.pow(math.sin(deltaLong / 2), 2)
      val greatCircleDistance = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
      val dist= 6370.9924 * greatCircleDistance
      println(s"line 52 - km distance between courier and order ${dist}" )
      dist <= 15
    }

    /**
     * checking if the scores are acceptable to make a delivery.
     * @param score1
     * @param score2
     * @return
     */
    def scoreCheck(score1:Double, score2:Double): Boolean ={
      if ((score1 + score2) > 9)
        true
      else
        false
    }


    def matchRec(record: JsValue): Unit ={
      try{
        //      courier_id,courier_score,app_created_timestamp,lat,lon
        val order_id = (record \ "order_id").get.as[String]
        val order_score = (record \ "order_score").get.as[String].toDouble
        val ord_app_created_timestamp = (record \ "app_created_timestamp").get.as[String]
        val ord_lat = (record \ "lat").get.as[String].toDouble
        val ord_lon = (record \ "lon").get.as[String].toDouble
        println("line 55 print rec..", order_id,order_score,ord_app_created_timestamp,ord_lat,ord_lon)

        println("line 73", (distanceCheck(cour_lat, cour_lon, ord_lat, ord_lon), scoreCheck(courier_score,order_score) ))

        if (distanceCheck(cour_lat, cour_lon, ord_lat, ord_lon) && scoreCheck(courier_score,order_score) ) {
          /**
           * match made, not appending the courier to the map, produce a message, and also delete
           * the matched order from the order map.
           */
          println("line 78", (distanceCheck(cour_lat, cour_lon, ord_lat, ord_lon), scoreCheck(courier_score,order_score) ))
        }

      } catch {
        case e: Exception => print(e)
      }
   }

    val matched = false
    main.orderRecords.foreach(
      rec => matchRec(rec._1)
    )

    main.appendCour(event.get, meta)
  }

  private def processOrder(message: ConsumerRecord[Array[Byte], String],
                           main: ProcessorMain.type) = {
    //    produceRawMessage(json) --produce raw message here
    val meta = Json.parse(message.value)
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
