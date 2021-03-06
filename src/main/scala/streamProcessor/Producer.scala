package streamProcessor

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.github.matsluni.akkahttpspi.AkkaHttpClient
import com.github.matsluni.akkahttpspi.AkkaHttpClient.logger
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest

import scala.concurrent.ExecutionException

/**
 * This is the producer to produce kinesis record to data stream
 * Java code, but good as an reference:
 * https://github.com/dingjie27/demoforKinesis/blob/454755a14a483c3af29d12ddab08cae25be045de/src/main/java/com/kinesis/demo/service/producer/ProducerUsingKinesisAsyncClient.java
 */
object Producer {
  val config = ConfigFactory.load()
  implicit val system: ActorSystem = ActorSystem("kinesis-producer")
  //  print(system.settings)

  val iam_accesskey = config.getConfig("kinesis-producer")
    .getValue("iam.accesskey")
    .unwrapped().toString
  val iam_password = config.getConfig("kinesis-producer")
    .getValue("iam.password")
    .unwrapped().toString

  implicit val materializer: Materializer = ActorMaterializer()
  val region = Region.US_WEST_2

  /**
   * setup async client
   */
  val credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(iam_accesskey, iam_password))
  implicit val amazonKinesisAsync: KinesisAsyncClient =
    KinesisAsyncClient
      .builder()
      .credentialsProvider(credentialsProvider)
      .httpClient(AkkaHttpClient.builder().withActorSystem(system).build())
      // Possibility to configure the retry policy
      // see https://doc.akka.io/docs/alpakka/current/aws-shared-configuration.html
      // .overrideConfiguration(...)
      .region(region)
      .build()


  def kinesisPutRecord(streamname: String, message: String, key: String): Unit = {
    kinesisPutRecord(this.amazonKinesisAsync, streamname: String, message: String, key: String)
  }

  /**
   * put records in the format as jsonline files
   * @param amazonKinesisAsync
   * @param streamname
   * @param message
   * @param key
   */
  private def kinesisPutRecord(amazonKinesisAsync: KinesisAsyncClient = this.amazonKinesisAsync, streamname: String, message: String, key: String): Unit = {
    val request: PutRecordRequest = PutRecordRequest.builder()
      .partitionKey(key)
      .streamName(streamname)
      .data(SdkBytes.fromByteArray((message+"\n").getBytes()))
      .build();

    amazonKinesisAsync.putRecord(request);
    try logger.info("Producing record msg number= {} , record sequence number {} ", 1, amazonKinesisAsync.putRecord(request).get.sequenceNumber)
    catch {
      case e: InterruptedException =>
        e.printStackTrace()
      case e: ExecutionException =>
        e.printStackTrace()
    }
  }

  def main(args: Array[String]): Unit = {
    LoggerFactory.getLogger(Producer.getClass)
    val message = "test messagehttps://s3.console.aws.amazon.com/s3/buckets/akka-demo-bucket?region=us-west-2&tab=objects"
    val key = s"partitionKey ${"iijflsd-123l-fjdls-123"}"
    val streamname = "akkademo-order"
    kinesisPutRecord(streamname, message, key)
    system.registerOnTermination(amazonKinesisAsync.close())
//    amazonKinesisAsync.close()
    System.exit(0)
  }
}