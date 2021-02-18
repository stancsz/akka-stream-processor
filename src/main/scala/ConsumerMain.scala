import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import processstream.ProcessMessage._

import scala.concurrent.Future
import scala.language.postfixOps

object ConsumerMain {

  def main(args: Array[String]): Unit = {
    println("starting")
    implicit val system:ActorSystem = ActorSystem.apply("akka-stream-kafka")
    implicit val materializer:ActorMaterializer = ActorMaterializer()

    // grab our settings from the resources/application.conf file
    val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)

    // our topic to subscribe to for messages
    val topic = "dbserver1.courier_order_db.CourierTest"
    val topic2 = "dbserver1.courier_order_db.OrderTest"

    var consumerRecordList: List[ConsumerRecord[Array[Byte], String]] = List[ConsumerRecord[Array[Byte], String]]()
    def processMessage(msg: ConsumerRecord[Array[Byte], String]) = {
      consumerRecordList = msg :: consumerRecordList
//      println(s"Message Received : ${msg.timestamp} - ${msg.value}")
      consumerRecordList.foreach {
        println
      }
    }

    // listen to our topic with our settings, until the program is exited
    Consumer.plainSource(consumerSettings, Subscriptions.topics(topic, topic2))
      .mapAsync(1) ( msg => {
        // print out our message once it's received
        processMessage(msg)
        printEntireConsumerRecordList(consumerRecordList)
        Future.successful(msg)
      }).runWith(Sink.ignore)
  }
}