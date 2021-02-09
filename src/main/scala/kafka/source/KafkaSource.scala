package kafka.source

import akka.projection.MergeableOffset
import akka.projection.kafka.scaladsl.KafkaSourceProvider
import akka.projection.scaladsl.SourceProvider

class KafkaSource {
  import akka.kafka.ConsumerSettings
  import org.apache.kafka.clients.consumer.ConsumerConfig
  import org.apache.kafka.clients.consumer.ConsumerRecord
  import org.apache.kafka.common.serialization.StringDeserializer
  val bootstrapServers = "localhost:9092"
  val groupId = "group-wordcount"
  val topicName = "words"
  val consumerSettings =
    ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(bootstrapServers)
      .withGroupId(groupId)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val sourceProvider: SourceProvider[MergeableOffset[JLong], ConsumerRecord[String, String]] =
    KafkaSourceProvider(system, consumerSettings, Set(topicName))
}
