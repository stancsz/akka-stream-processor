package streamProcessor

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import play.api.libs.json.JsValue

import scala.concurrent.Future

object Processor {

  var producer = Producer

  var courMessage: JsValue = null
  var ordMessage: JsValue = null
  var matchMessage: JsValue = null

  var courierRecords: Map[JsValue, JsValue] = Map()
  var orderRecords: Map[JsValue, JsValue] = Map()

  def setCourMessage(msg: JsValue): Unit = {
    courMessage = msg
  }

  def setOrdMessage(msg: JsValue): Unit = {
    ordMessage = msg
  }

  def setMatchMessage(msg: JsValue): Unit = {
    matchMessage = msg
  }

  def appendCour(key: JsValue, value: JsValue): Unit = {
    courierRecords = courierRecords + (key -> value)
  }

  def appendOrder(key: JsValue, value: JsValue): Unit = {
    orderRecords = orderRecords + (key -> value)
  }

  def removeFromCour(key: JsValue): Unit = {
    courierRecords = courierRecords.-(key)
  }

  def removeFromOrder(key: JsValue): Unit = {
    orderRecords = orderRecords.-(key)
  }

  def main(args: Array[String]): Unit = {
    println("Processor Main Started...")
    implicit val system: ActorSystem = ActorSystem.apply("akka-stream-kafka")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    // grab our settings from the resources/application.conf file
    val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)

    // our topic to subscribe to for messages
    val topic = "dbserver1.courier_order_db.CourierTest"
    val topic2 = "dbserver1.courier_order_db.OrderTest"

    // listen to our topic with our settings, until the program is exited
    Consumer.plainSource(consumerSettings, Subscriptions.topics(topic, topic2))
      .mapAsync(1)(msg => {
        ProcessStream.process(this, msg)
        Future.successful(msg)
      }).runWith(Sink.ignore)

  }

}
