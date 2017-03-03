package com.logic.util.db

import com.logic.util.ResourcesUtil
import com.logic.util.content.ConfigContent
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.mapred.JobConf
import org.apache.hadoop.hbase.client.Connection
import org.apache.hadoop.hbase.client.HBaseAdmin

/**
  * Hbase工具类
  * user:刘宪领
  */
object HbasePool {
  private val quorum = ResourcesUtil.getVal(ConfigContent.HBASE_ZOOKEEPER_QUORUM)
  private val port = ResourcesUtil.getVal(ConfigContent.ZOOKEEPER_PORT)
  private val conf = HBaseConfiguration.create()
  private val jobConf = new JobConf(getConfiguration(),this.getClass)
  
  def getHConn():Connection ={
    ConnectionFactory.createConnection(conf)
  }
  
  def getConfiguration() ={
    conf.set(ConfigContent.HBASE_ZOOKEEPER_QUORUM,quorum)
    conf.set(ConfigContent.ZOOKEEPER_PORT,port)
   /* conf.set("hbase.master", "centos.host1:60000")   
    conf.addResource("/home/hadoop/software/hbase-0.92.2/conf/hbase-site.xml") */
    conf
  }
  def getJobConf() ={
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf
  }
}
