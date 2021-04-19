package creditCardTransactionsStreaming

import org.apache.spark.sql.SparkSession

object testKey {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .config("spark.master", "local")
      .getOrCreate()

    val df = spark.read.json("src/main/resources/example-key-17e2bf6eba20.json")

    // Displays the content of the DataFrame to stdout
    df.printSchema()
    df.show()
  }

}
