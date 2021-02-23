package streamProcessor

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future

object ProcessorMain {

  var courierRecords: Map[JsValue, JsValue] = Map()
  var orderRecords: Map[JsValue, JsValue] = Map()

  def appendCour(key: JsValue, value: JsValue): Unit = {
    courierRecords = courierRecords + (key -> value)
  }

  def appendOrder(key: JsValue, value: JsValue): Unit = {
    orderRecords = orderRecords + (key -> value)
  }

  def removeFromCour(key:JsValue): Unit ={
    courierRecords = courierRecords.-(key)
  }

  def removeFromOrder(key:JsValue): Unit ={
    orderRecords = orderRecords.-(key)
  }


  def processMessage(msg: ConsumerRecord[Array[Byte], String]) = {
    println(s"Message Received : ${msg.timestamp} - ${msg.value}")
    println(s"Value: ${msg.value.getClass}")
    val json = Json.parse(msg.value) // https://circe.github.io/circe/parsing.html
    println(json)
    println(json \ "payload" \ "after" \ "order_id")
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
        RecordProcessor.process(this, msg)
        Future.successful(msg)
      }).runWith(Sink.ignore)
  }
}
