package com.logic.bigdata.spark.hdfs

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object HDFSLook {

  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setAppName("HDFSLook")
    val sparkContext=new SparkContext(conf)
  }
}
