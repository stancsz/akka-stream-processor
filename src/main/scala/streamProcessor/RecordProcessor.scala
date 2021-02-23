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
    println(s"${Instant.now().toString()} started processing topic... ${topic}")
    topic match {
      case "CourierTest" => {
        processCourier(message, main)
      }
      case "OrderTest" => {
        processOrder(message, main)
      }
    }

  }

  /**
   * match the distance between coordinates, and determine if the distance is less than
   * the required distance by business logic.
   *
   * @param lat1
   * @param lon1
   * @param lat2
   * @param lon2
   */
  def distanceCheck(lat1: Double, lon1: Double, lat2: Double, lon2: Double, distance: Double): Boolean = {
    val deltaLat = math.toRadians(abs(lat1 - lat2))
    val deltaLong = math.toRadians(abs(lon1 - lon2))
    val a = math.pow(math.sin(deltaLat / 2), 2) + math.cos(math.toRadians(lat1)) * math.cos(math.toRadians(lat2)) * math.pow(math.sin(deltaLong / 2), 2)
    val greatCircleDistance = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    val dist = 6370.9924 * greatCircleDistance
    println(s"line 52 - km distance between courier and order ${dist}")
    dist <= distance
  }

  /**
   * checking if the scores are acceptable to make a delivery.
   *
   * @param score1
   * @param score2
   * @return
   */
  def scoreCheck(score1: Double, score2: Double): Boolean = {
    if ((score1 + score2) > 9)
      true
    else
      false
  }


  private def processCourier(message: ConsumerRecord[Array[Byte], String],
                             main: ProcessorMain.type) = {
    //    produceRawMessage(json) -- produce raw message here
    val meta = Json.parse(message.value)
    val event = (meta \ "payload" \ "after")
    val courier_id = (event \ "courier_id").get.as[String]
    val courier_score = (event \ "courier_score").get.as[String].toDouble
    val cour_app_created_timestamp = (event \ "app_created_timestamp").get.as[String]
    val cour_lat = (event \ "lat").get.as[String].toDouble
    val cour_lon = (event \ "lon").get.as[String].toDouble


    def matchOrder(record: JsValue): Unit = {
      try {
        val order_id = (record \ "order_id").get.as[String]
        val order_score = (record \ "order_score").get.as[String].toDouble
        val ord_app_created_timestamp = (record \ "app_created_timestamp").get.as[String]
        val ord_lat = (record \ "lat").get.as[String].toDouble
        val ord_lon = (record \ "lon").get.as[String].toDouble
        if (distanceCheck(cour_lat, cour_lon, ord_lat, ord_lon, 15) && scoreCheck(courier_score, order_score)) {
          /**
           * match made, not appending the courier to the map, produce a matched message, and also delete
           * the matched order from the order map.
           */
          println(s"line 85 - match made - distance check: ${distanceCheck(cour_lat, cour_lon, ord_lat, ord_lon, 15)}  , score check: ${scoreCheck(courier_score, order_score)} )")
          println(s"dropping match record before: ${main.orderRecords}")
          main.removeFromOrder(record)
          //          produceMatchedMessage(event.get, record)
          println(s"dropping match record after: ${main.orderRecords}")
        } else {
          /**
           * match not made, appending the new courier event and meta data to the courier map.
           */
          println(s"line 90 - match not made - distance check: ${distanceCheck(cour_lat, cour_lon, ord_lat, ord_lon, 15)}  , score check: ${scoreCheck(courier_score, order_score)} )")
          main.appendCour(event.get, meta)
          println(s"line 92 - non matched record added to courier map: ${main.courierRecords}")
        }

      } catch {
        case e: Exception => print(e)
      }
    }

    main.orderRecords.foreach(
      rec => matchOrder(rec._1)
    )

  }

  private def processOrder(message: ConsumerRecord[Array[Byte], String],
                           main: ProcessorMain.type) = {
    val meta = Json.parse(message.value)
    val event = (meta \ "payload" \ "after")
    val order_id = (event \ "order_id").get.as[String]
    val order_score = (event \ "order_score").get.as[String].toDouble
    val ord_app_created_timestamp = (event \ "app_created_timestamp").get.as[String]
    val ord_lat = (event \ "lat").get.as[String].toDouble
    val ord_lon = (event \ "lon").get.as[String].toDouble


    def matchCourier(record: JsValue): Unit = {
      try {
        val courier_id = (record \ "courier_id").get.as[String]
        val courier_score = (record \ "courier_score").get.as[String].toDouble
        val cour_app_created_timestamp = (record \ "app_created_timestamp").get.as[String]
        val cour_lat = (record \ "lat").get.as[String].toDouble
        val cour_lon = (record \ "lon").get.as[String].toDouble
        if (distanceCheck(cour_lat, cour_lon, ord_lat, ord_lon, 15) && scoreCheck(courier_score, order_score)) {
          /**
           * match made, not appending the order to the map, produce a matched message, and also delete
           * the matched courier from the courier map.
           */
          println(s"line 85 - match made - distance check: ${distanceCheck(cour_lat, cour_lon, ord_lat, ord_lon, 15)}  , score check: ${scoreCheck(courier_score, order_score)} )")
          println(s"dropping match record before: ${main.courierRecords}")
          main.removeFromCour(record)
          //          produceMatchedMessage(event.get, record)
          println(s"dropping match record after: ${main.courierRecords}")
        } else {
          /**
           * match not made, appending the new courier event and meta data to the courier map.
           */
          println(s"line 90 - match not made - distance check: ${distanceCheck(cour_lat, cour_lon, ord_lat, ord_lon, 15)}  , score check: ${scoreCheck(courier_score, order_score)} )")
          main.appendOrder(event.get, meta)
          println(s"line 92 - non matched record added to order map: ${main.orderRecords}")
        }

      } catch {
        case e: Exception => print(e)
      }
    }

    main.courierRecords.foreach(
      rec => matchCourier(rec._1)
    )
  }

}
