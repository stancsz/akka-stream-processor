//Copyright (c) Microsoft Corporation. All rights reserved.
//Licensed under the MIT License.
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

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

    // listen to our topic with our settings, until the program is exited
    Consumer.plainSource(consumerSettings, Subscriptions.topics(topic, topic2))
      .mapAsync(1) ( msg => {
        // print out our message once it's received
        println(s"Message Received : ${msg.timestamp} - ${msg.value}")
        Future.successful(msg)
      }).runWith(Sink.ignore)
  }
}