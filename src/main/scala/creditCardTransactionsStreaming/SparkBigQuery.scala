package creditCardTransactionsStreaming

/**
 * spark bigquery connector guide
 * https://cloud.google.com/dataproc/docs/tutoials/bigquery-connector-spark-example
 * https://github.com/GoogleCloudDataproc/spark-bigquery-connector
 */
import com.github.matsluni.akkahttpspi.AkkaHttpClient.logger
import com.google.cloud.spark
import org.apache.spark.sql.SparkSession

object SparkBigQuery {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("spark-bigquery-demo")
      .config("spark.master", "local")
      .getOrCreate()

    spark.read
      .format("bigquery")
      .option("credentialsFile", "src/main/resources/bottlerocket-dev-2449233c0fcb.json")
  }

}
