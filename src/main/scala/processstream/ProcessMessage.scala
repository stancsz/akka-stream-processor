package processstream
import org.apache.kafka.clients.consumer.ConsumerRecord

object ProcessMessage {
  def actorAction(processList: List[ConsumerRecord[Array[Byte], String]]): Unit = {
    processList.foreach {
      println
    }
//    printEntireConsumerRecordList(processList)
    return processList
  }

  def printEntireConsumerRecordList(processList: List[ConsumerRecord[Array[Byte], String]]) = {
    println("------")
//    processList.foreach {
//      println
//    }
    println(processList)
    println("------")
  }


}
