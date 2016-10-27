package com.logic.bigdata.spark.hdfs

import java.util.{Date, Properties}

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConversions._

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object HDFSLook {

  def main(args: Array[String]): Unit = {
    // 关闭日志输出
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val conf = new SparkConf().setAppName("HDFSLook")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val data = scanfData(sqlContext).cache()
    println("end=================" + data.count())
    val properties = new Properties()
    properties += ("user" -> "ceshi")
    properties += ("password" -> "ceshi")
    data.write.jdbc("jdbc:mysql://192.168.1.105:3306/ml_ex_ceshi?zeroDateTimeBehavior=convertToNull", "ceshiku", properties)
  }

  def scanfData(sqlContext: SQLContext): DataFrame = {
    val filePath = "hdfs://192.168.1.101:8020/test/feature/feature/"
    //feature_result_data_20161027-111121
    val fileName = "feature_result_data_"
    val nowTime = new Date()
    val hconf = sqlContext.sparkContext.hadoopConfiguration
    hconf.set("fs.defaultFS", "hdfs://192.168.1.101:8020")
    val fs = FileSystem.newInstance(hconf)
    val statusList = fs.listStatus(new Path(filePath))
    println("=============" + statusList.size)
    var data = sqlContext.read.load(statusList.array(0).getPath.toString).selectExpr("province_id", "goods_id", "score", "2018000000 as timer")
    for (status <- statusList) {
      val path = status.getPath.toString
      val timer = path.substring(path.length - 15, path.length - 4).replace("-","")
      data = data.unionAll(sqlContext.read.load(status.getPath.toString).selectExpr("province_id", "goods_id", "score", timer + " as timer"))
    }
    data
  }
}
