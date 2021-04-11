package creditCardTransactionsStreaming
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object CSVModel {
  /**
   * https://alvinalexander.com/scala/csv-file-how-to-process-open-read-parse-in-scala/
   * pass the path as argument for testing i.e.
   * 'fs:/status=unprocessed/topic=credit_card_transaction/version=1.0-demo/created=2021-04-10/bq-results-20210410-010225-sfhdnfwi5s8q.csv'
   */

  var path : String = _ // TODO set default value if not passing arguments

  def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  def processCSV(path: String) = {
    // each row is an array of strings (the columns in the csv file)
    val rows = ArrayBuffer[Array[String]]()

    // (1) read the csv data
    using(Source.fromFile(path)) { source =>
      for (line <- source.getLines) {
        rows += line.split(",").map(_.trim)
      }
    }

    // (2) print the results
    for (row <- rows) {
      println(s"${row(0)}|${row(1)}|${row(2)}|${row(3)}")
    }
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
    } else {
      path = args(0)
    }

    processCSV(path)
  }
}
