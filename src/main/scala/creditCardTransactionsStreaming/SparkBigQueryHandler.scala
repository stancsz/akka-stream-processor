package creditCardTransactionsStreaming

import org.apache.spark.sql.SparkSession

object SparkBigQueryHandler {
  val spark = SparkSession.builder()
    .appName("spark-bigquery-demo")
    .config("spark.master", "local")
    .getOrCreate()

  /**
   * alternatively,
   * spark.conf.set("credentials","<SERVICE_ACCOUNT_JSON_IN_BASE64>")
   */
  spark.conf.set("credentialsFile", "src/main/resources/bottlerocket-dev-2449233c0fcb-jsonline.json")


  def main(args: Array[String]): Unit = {
    /**
     * Example usage
     * spark.read.format("bigquery").option("credentialsFile", "src/main/resources/bottlerocket-dev-2449233c0fcb-jsonline.json")
     * .bigquery("bigquery-public-data.samples.shakespeare")
     *
     * Error: sun.misc.Unsafe or java.nio.DirectByteBuffer.<init>(long, int) not available
     * fix: use jdk 1.8, i.e. adopt-openjdk-1.8.0_282
     *
     */

    val wordsDF = spark.read.format("bigquery")
      .option("table", "bottlerocket-dev:ml_datasets.ulb_fraud_detection")
      .load()
      .cache()
    wordsDF.createOrReplaceTempView("words")


    //    bqResult.collect().foreach(println)
    //    show(bqResult.count())
    wordsDF.show(35)
  }
}
