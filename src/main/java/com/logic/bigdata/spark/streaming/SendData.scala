package com.logic.bigdata.spark.streaming

import java.net.ServerSocket

import org.apache.spark.{SparkConf, SparkContext}


/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object SendData {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("sendData")
    val sparkContext = new SparkContext(sparkConf)
    val data = sparkContext.textFile("hdfs://192.168.1.101:8020/test/dayLog.out")
    val lines = data.collect()
    val socketServer = new ServerSocket(6666)
    while (true) {
      val socket = socketServer.accept()
      val out = socket.getOutputStream
      while (true) {
        lines.foreach(line => {
          Thread.sleep(1000 * 3)
          println(line)
          out.write((line + "\n").getBytes)
          out.flush()
        })
      }
      out.close()
    }

  }
}

