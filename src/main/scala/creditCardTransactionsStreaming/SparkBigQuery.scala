package creditCardTransactionsStreaming

/**
 * spark bigquery connector guide
 * https://cloud.google.com/dataproc/docs/tutoials/bigquery-connector-spark-example
 * https://github.com/GoogleCloudDataproc/spark-bigquery-connector
 */
import com.github.matsluni.akkahttpspi.AkkaHttpClient.logger
import com.google.cloud.spark
import org.apache.spark.sql.SparkSession
import com.google.cloud.spark.bigquery._
import org.apache.spark.sql.catalyst.ScalaReflection.universe.show

object SparkBigQuery {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("spark-bigquery-demo")
      .config("spark.master", "local")
      .getOrCreate()

    /**
     * Example usage
     spark.read.format("bigquery").option("credentialsFile", "src/main/resources/bottlerocket-dev-2449233c0fcb.json")
      .bigquery("bigquery-public-data.samples.shakespeare")

     */
    val bqResult = spark.read
      .format("bigquery")
      .option("credentialsFile", "src/main/resources/bottlerocket-dev-2449233c0fcb.json")
      .bigquery("bigquery-public-data.samples.shakespeare")

//    bqResult.collect().foreach(println)
//    show(bqResult.count())
    bqResult.show(35)
  }

}
