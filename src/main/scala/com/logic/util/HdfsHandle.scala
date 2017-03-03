package com.logic.util

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * 读取Hadoop文件，
  */
object HdfsHandle {

  /**
    * 读取HDFS文件
    * @param path
    * @param sc
    * @return
    */
  def readFile(path:String,sc:SparkContext): RDD[String] ={

    val rawData = sc.textFile(path)
    rawData
  }    

}
