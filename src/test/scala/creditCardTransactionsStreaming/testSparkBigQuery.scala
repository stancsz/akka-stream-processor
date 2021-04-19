package creditCardTransactionsStreaming

import org.apache.spark.sql.SparkSession

object testSparkBigQuery {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("spark-bigquery-demo")
      .config("spark.master", "local")
      .getOrCreate()

    /**
     * alternatively,
     * spark.conf.set("credentials","<SERVICE_ACCOUNT_JSON_IN_BASE64>")
     */
    spark.conf.set("credentialsFile", "src/main/resources/bottlerocket-dev-2449233c0fcb-jsonline.json")


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
      .option("table", "bigquery-public-data:samples.shakespeare")
      .load()
      .cache()
    wordsDF.createOrReplaceTempView("words")


    //    bqResult.collect().foreach(println)
    //    show(bqResult.count())
    wordsDF.show(35)
  }

}
