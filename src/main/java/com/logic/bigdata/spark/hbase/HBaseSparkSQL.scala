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
    var hbaseParams=Map[String,String]()
    hbaseParams+=("sparksql_table_schema"->"row_key string, a string, b string, c string")
    hbaseParams+=("hbase_table_name"->"wgd_test")
    hbaseParams+=("hbase_table_schema"->"(:key , wgd:a , wgd:b , wgd:c )")

//    val sqlContext=new SQLContext(sc)
//    val hbaseTable=sqlContext.read.format("").options(hbaseParams).load()
//    hbaseTable.show()

    //hbase
//    val hbaseConfig=HBaseConfiguration.create()
//    val hbaseContext=new HBaseContext(sc,hbaseConfig)
//    val scan=new Scan()
//    val tableName=new TableName()
//    hbaseContext.hbaseRDD(tableName,scan)
//    hbaseContext.

  }

}
