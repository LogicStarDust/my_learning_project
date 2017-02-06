package com.logic.bigdata.spark.streaming

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object StreamingForKafka {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("StreamingForKafka")
    val streamingContext = new StreamingContext(conf, Seconds(2))
    // Kafka configurations
    val topics = Set("richinfo")
    val brokers = "192.168.1.103:9092"
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers)

    val streaming = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](streamingContext, kafkaParams, topics)
    streaming.print()
//    streaming.foreachRDD { rdd =>
//      println("one RDD")
//      rdd.foreach(record => println(record._2))
//    }

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
