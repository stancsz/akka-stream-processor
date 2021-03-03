package streamProcessor

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.github.matsluni.akkahttpspi.AkkaHttpClient
import com.github.matsluni.akkahttpspi.AkkaHttpClient.logger
import org.slf4j.LoggerFactory
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.regions.Region

/**
 *
 * Java code, but good as an reference:
 * https://github.com/dingjie27/demoforKinesis/blob/454755a14a483c3af29d12ddab08cae25be045de/src/main/java/com/kinesis/demo/service/producer/ProducerUsingKinesisAsyncClient.java
 */
object ProduceSingleMessage {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: Materializer = ActorMaterializer()

    val streamname="akkademo-order"
    val region = Region.US_WEST_2

    /**
     * setup async client
     */
    implicit val amazonKinesisAsync: software.amazon.awssdk.services.kinesis.KinesisAsyncClient =
      KinesisAsyncClient
        .builder()
        .httpClient(AkkaHttpClient.builder().withActorSystem(system).build())
        // Possibility to configure the retry policy
        // see https://doc.akka.io/docs/alpakka/current/aws-shared-configuration.html
        // .overrideConfiguration(...)
        .region(region)
        .build()


    LoggerFactory.getLogger(ProduceSingleMessage.getClass)


    val message = "test message"

    val request: PutRecordRequest = PutRecordRequest.builder()
      .partitionKey(String.format("partitionKey-%d", 1.toString))
      .streamName(streamname)
      .data(SdkBytes.fromByteArray(message.getBytes()))
      .build();

    amazonKinesisAsync.putRecord(request);


    import java.util.concurrent.ExecutionException
    try logger.info("Producing record msg number= {} , record sequence number {} ", 1, amazonKinesisAsync.putRecord(request).get.sequenceNumber)
    catch {
      case e: InterruptedException =>
        e.printStackTrace()
      case e: ExecutionException =>
        e.printStackTrace()
    }


    system.registerOnTermination(amazonKinesisAsync.close())
  }


}