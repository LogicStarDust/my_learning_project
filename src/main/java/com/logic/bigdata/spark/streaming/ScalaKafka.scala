package com.logic.bigdata.spark.streaming


import java.util
import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}


/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object ScalaKafka {

  def main(args: Array[String]): Unit = {
    val props=new Properties()
    props.put("bootstrap.servers","192.168.1.104:9092")
//    props.put("acks", "all")
//    props.put("retries", 0.toString)
//    props.put("batch.size", 16384.toString)
//    props.put("linger.ms", 1.toString)
//    props.put("buffer.memory", 33554432.toString)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val product=new KafkaProducer[String,String](props)
    println("put ")
    for(i <- 1 to 10){

    product.send(new ProducerRecord[String,String]("test3","wuqian","wuqian"))
    }
    println("end")
    product.close()
  }
}
