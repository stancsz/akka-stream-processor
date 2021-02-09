package kafka.consumer

import akka.Done

import scala.concurrent.Future

class Rocket {
  def launch(destination: String): Future[Done] = {
    println(s"Rocket launched to $destination")
    Future.successful(Done)
  }
}
