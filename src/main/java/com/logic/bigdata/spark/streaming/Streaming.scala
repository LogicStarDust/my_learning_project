package com.logic.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  *
  *
  */
object Streaming {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("streaming")
    val streamingContext = new StreamingContext(sparkConf, Seconds(5))

    val lines = streamingContext.socketTextStream("192.168.3.3", 6666)

    lines.print()

    streamingContext.start()
    streamingContext.awaitTermination()

  }
}
