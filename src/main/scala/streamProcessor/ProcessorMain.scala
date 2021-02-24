package streamProcessor

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.alpakka.kinesis.KinesisFlowSettings
import akka.stream.alpakka.kinesis.scaladsl.KinesisSink
import akka.stream.scaladsl.Sink
import com.github.matsluni.akkahttpspi.AkkaHttpClient
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import play.api.libs.json.JsValue
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.PutRecordsRequestEntry

import scala.concurrent.Future

object ProcessorMain {
  var courMessage :JsValue = null
  var ordMessage :JsValue = null
  var matchMessage :JsValue = null

  var courierRecords: Map[JsValue, JsValue] = Map()
  var orderRecords: Map[JsValue, JsValue] = Map()

  def setCourMessage(msg : JsValue ): Unit ={
    courMessage = msg
  }

  def setOrdMessage(msg : JsValue ): Unit ={
    ordMessage = msg
  }

  def setMatchMessage(msg : JsValue ): Unit ={
    matchMessage = msg
  }

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


  def produceMessage() = {
    if (courMessage!=null){
      //produce something
    } else if (ordMessage!=null){
      //produce something
    } else if (matchMessage!=null){
      //produce something
    }

  }

  def main(args: Array[String]): Unit = {
    println("Processor Main Started...")
    implicit val system: ActorSystem = ActorSystem.apply("akka-stream-kafka")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    /**
     * aws client config
     */
    implicit val amazonKinesisAsync: software.amazon.awssdk.services.kinesis.KinesisAsyncClient =
      KinesisAsyncClient
        .builder()
        .httpClient(AkkaHttpClient.builder().withActorSystem(system).build())
        // Possibility to configure the retry policy
        // see https://doc.akka.io/docs/alpakka/current/aws-shared-configuration.html
        // .overrideConfiguration(...)
        .build()


    /**
     * kinesis config
     */
    val flowSettings = KinesisFlowSettings
      .create()
      .withParallelism(1)
      .withMaxBatchSize(500)
      .withMaxRecordsPerSecond(1000)
      .withMaxBytesPerSecond(1000000)
    val defaultFlowSettings = KinesisFlowSettings.Defaults
    val shardFlowSettings = KinesisFlowSettings.byNumberOfShards(1)

    /**
     * kinesis flow config
     */
    val sinkOrd: Sink[PutRecordsRequestEntry, NotUsed] = KinesisSink("akkademo-order")
    val sinkCour: Sink[PutRecordsRequestEntry, NotUsed] = KinesisSink("akkademo-cour")
    val sinkmatch: Sink[PutRecordsRequestEntry, NotUsed] = KinesisSink("akkademo-match")



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


    /**
     * terminating kinesis client
     */
    system.registerOnTermination(amazonKinesisAsync.close())
  }

}
