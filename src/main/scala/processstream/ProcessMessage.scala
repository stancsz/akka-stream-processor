package processstream
import org.apache.kafka.clients.consumer.ConsumerRecord

object ProcessMessage {
  def actorAction(records: Map[String, ConsumerRecord[Array[Byte], String]]): Unit = {
    records.foreach {
//      do something
      println
    }
  }

  def printRecords(records: Map[String, ConsumerRecord[Array[Byte], String]]) = {
    println("------")
//    first print row by row
    records.foreach {
      println
    }
//    then print entirely
    println("------")
    println(records)
    println("------")
  }


}
