package com.logic.bigdata.spark.json

import com.logic.functional.programming.chapter3.Test3_1until6
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object DataFromJson {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("clean")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val data=sqlContext.read.format("json").load("hdfs://192.168.1.104:8020/test/searchData.log")
  Test3_1until6.getClass
    println(data.schema)
  }

}
