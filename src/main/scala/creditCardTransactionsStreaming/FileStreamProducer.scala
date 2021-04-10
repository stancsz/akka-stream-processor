package creditCardTransactionsStreaming

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.file.scaladsl.DirectoryChangesSource

import java.nio.file.FileSystems
import scala.concurrent.duration._

object FileStreamProducer {
  /**
   * var path:String = "fs:/status=unprocessed/topic=credit_card_transaction/version=1.0-demo/created=2021-04-10"
   * var destpath: String = "fs:/status=processed/topic=credit_card_transaction/version=1.0-demo/created=2021-04-10"
   */

  var path: String = _
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
    } else {
      path = args(0)
    } //throw new IllegalArgumentException("Usage: DirectoryChangesSourceTest [path]")
    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer = ActorMaterializer()

    // #minimal-sample
    val fs = FileSystems.getDefault
    val changes = DirectoryChangesSource(fs.getPath(path), pollInterval = 1.second, maxBufferSize = 1000)
    changes.runForeach {
      case (path, change) => println("Path: " + path + ", Change: " + change)
    }
    // #minimal-sample

  }
}


