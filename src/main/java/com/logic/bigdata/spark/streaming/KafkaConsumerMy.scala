package com.logic.bigdata.spark.streaming

import java.util.Properties

import kafka.consumer.ConsumerConfig

import scala.collection.mutable.Map

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object KafkaConsumerMy extends Thread {

  var topic = "richinfo"
  var consumer = kafka.consumer.Consumer.create(createConsumerConfig)

  def createConsumerConfig: ConsumerConfig = {
    val props = new Properties()
    props.put("zookeeper.connect", "192.168.1.104:2181,192.168.1.101:2181,192.168.1.103:2181,server104:9092")
    props.put("group.id", "0")
    props.put("zookeeper.session.timeout.ms", "10000")
    props.put("zookeeper.sync.time.ms", "200")
    props.put("auto.commit.interval.ms", "1000")
    new ConsumerConfig(props)
  }


  override def run: Unit = {
    println("=====================start")
    val topicsMap: Map[String, Int] = Map()
    topicsMap += (topic -> 1)
    val streamMap = consumer.createMessageStreams(topicsMap)
    println("=====================streamMap"+streamMap.size+","+streamMap.get(topic).get(0).size)
    val stream = streamMap.get(topic).get(0)
    val it = stream.iterator()
    println("================result=========="+stream.size)
    while (it.hasNext()) {
      println(Thread.currentThread() + "get data:" + it.next().message())
    }
    try {
      Thread.sleep(1000)
    } catch {
      case e: InterruptedException => e.printStackTrace
    }
  }

  def main(args: Array[String]): Unit = {
    KafkaConsumerMy.start()
  }
}
