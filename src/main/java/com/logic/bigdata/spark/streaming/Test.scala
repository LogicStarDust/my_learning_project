package com.logic.bigdata.spark.streaming

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Test {
  def main(args: Array[String]): Unit = {
    val c = new KafkaConsumer("richinfo", "1", "192.168.1.104:2181")
    c.read(x => println("*************"+new String(x)))
  }
}
