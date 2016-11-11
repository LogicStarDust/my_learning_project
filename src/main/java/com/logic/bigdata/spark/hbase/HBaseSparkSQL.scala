package com.logic.bigdata.spark.hbase

import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.spark.HBaseContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

import scala.collection.JavaConversions._
/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object HBaseSparkSQL {
  def main(args: Array[String]): Unit = {
    //建立spark streaming输入流
    val conf=new SparkConf().setAppName("test")
    val sc=new SparkContext(conf)
    //hbase
    val hbaseConfig=HBaseConfiguration.create()
    hbaseConfig.set("hbase.zookeeper.quorum", "server104:2181,server101:2181,server103:2181")
    hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181")
    hbaseConfig.set("mapred.task.timeout", "1")

    val hbaseContext=new HBaseContext(sc,hbaseConfig)
    val scan=new Scan()
    scan.addFamily("wgd".getBytes)
    val data=hbaseContext.hbaseRDD(TableName.valueOf("wgd_test"),scan)

    println(data.count())
    data.foreach(x=>{
    println(new String(x._1.get)+":"+new String(x._2.getValue("wgd".getBytes,"a".getBytes)))
    })
//    hbaseContext.bulkPut()

  }

}
