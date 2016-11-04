package com.logic.bigdata.spark.hbase

import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Get, HTable}


/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object HBaseCRUD {
  def main(args: Array[String]): Unit = {

    val tableName=TableName.valueOf("wgd_test")
    val conf=HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", "server104:2181,server101:2181,server103:2181")
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("mapred.task.timeout", "1")

    val con= ConnectionFactory.createConnection(conf)
    val table=con.getTable(tableName)
    val result=table.get(new Get("key1".getBytes))

    println(result.size())
    println(new String(result.getValue("wgd".getBytes,"b".getBytes)))



  }

}
