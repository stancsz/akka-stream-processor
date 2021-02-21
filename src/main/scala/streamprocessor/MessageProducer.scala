package streamprocessor

import play.api.libs.json.JsValue

object MessageProducer {
  /**
   * get a json and then produce a message to sink.
   * In this case we're either using kinesis/firehose--> redhisft or
   * google bigquery equivalent
   */
def produceRawMessage(json: JsValue) = ???
}
