package com.logic.util.db

import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.filter.{CompareFilter, PrefixFilter, RegexStringComparator, RowFilter}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.{Base64, Bytes}
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import se.hbase.help.PartitionRowKeyManager

import scala.reflect.ClassTag

/**
  * Hbase操作类
  * user:刘宪领
  *
  * @param conn      org.apache.hadoop.hbase.client下的hbase连接
  * @param tableName 表名
  * @param family    列簇
  */
abstract class HbaseAccess[T: ClassTag](conn: Connection, tableName: String, family: String) {
  //数据转换器 把某一类型的数据转换成插入hbase表的数据类型
  val convert: ((T) => (ImmutableBytesWritable, Put))
  //数据解析器 把读取的hbase数据的类型转换成指定的类型
  val conReverse: (((ImmutableBytesWritable, Result)) => T)

  /**
    * 创建Hbase表
    *
    */
  def createHTable(splitKey: Int): Unit = {
    //Hbase表模式管理器
    val admin = conn.getAdmin
    val table = TableName.valueOf(tableName)
    if (!admin.tableExists(table)) {
      //创建Hbase表模式
      val tableDescriptor = new HTableDescriptor(table)

          val f = new HColumnDescriptor(family.getBytes())
          f.setMaxVersions(1)
          tableDescriptor.addFamily(f)

      //创建表
      //admin.createTable(tableDescriptor)
      val rkManager = new PartitionRowKeyManager();
      //只预建10个分区
      rkManager.setPartition(splitKey);
      val splitKeys = rkManager.calcSplitKeys();
      admin.createTable(tableDescriptor,splitKeys )
    }
  }
  def createHTable(familys: Array[String]): Unit = {
    //Hbase表模式管理器
    val admin = conn.getAdmin
    val table = TableName.valueOf(tableName)
    if (!admin.tableExists(table)) {
      //创建Hbase表模式
      val tableDescriptor = new HTableDescriptor(table)
      if (familys.nonEmpty) {
        for (col: String <- familys) {
          //创建列簇
          val f = new HColumnDescriptor(col.getBytes())
          f.setMaxVersions(1)
          tableDescriptor.addFamily(f)
        }
      }
      //创建表
      admin.createTable(tableDescriptor)

    }
  }
  /**
    * 创建Hbase表（使用类名中接受的family）
    */
  def createHTable(): Unit = {
    //Hbase表模式管理器
    val admin = conn.getAdmin
    val table = TableName.valueOf(tableName)
    if (!admin.tableExists(table)) {
      //创建Hbase表模式
      val tableDescriptor = new HTableDescriptor(table)
      //创建列簇
      val f = new HColumnDescriptor(family.getBytes())
      f.setMaxVersions(1)
      tableDescriptor.addFamily(f)
      //创建表
      admin.createTable(tableDescriptor)
    }
  }

  /**
    * 删除表
    *
    */
  def deleteHTable(): Unit = {
    //本例将操作的表名
    val table = TableName.valueOf(tableName)
    //Hbase表模式管理器
    val admin = conn.getAdmin
    if (admin.tableExists(table)) {
      admin.disableTable(table)
      admin.deleteTable(table)
    }

  }

  /**
    * 查询一条记录
    *
    * @param column 列名
    * @param key    rowKey名
    * @return 数据内容
    */
  def getAResult(column: String, key: String): String = {
    var table: Table = null
    try {
      val userTable = TableName.valueOf(tableName)
      table = conn.getTable(userTable)
      val g = new Get(key.getBytes())
      val result = table.get(g)
      val value = Bytes.toString(result.getValue(family.getBytes(), column.getBytes()))
      value
    } finally {
      if (table != null) table.close()

    }

  }


  /**
    * 插入数据
    *
    * @param rdd 插入的数据RDD集合
    */
  def inserAll(rdd: RDD[T]): Unit = {
    val jobconf = HbasePool.getJobConf()
    jobconf.set(TableOutputFormat.OUTPUT_TABLE, tableName)
    rdd.map(convert).saveAsHadoopDataset(HbasePool.getJobConf())
  }


  /**
    * 模糊查询
    *
    * @param sc        SparkContent：spark的上下文
    * @param rowKeyPre rowkey的前缀
    * @param columns   需要查询的列名
    * @return 查询的数据rdd集合
    */
  def getData(sc: SparkContext, rowKeyPre: String, columns: Array[String]): RDD[T] = {
    val scan = getPreScanString(rowKeyPre)
    val conf = HbasePool.getConfiguration()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN, scan)
    val hrdd = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    hrdd.map(conReverse)

  }


  /**
    *
    * 前缀-过滤器
    *
    * @param pre rowkey的前缀
    * @return 编码后的前缀
    */
  def getPreScanString(pre: String): String = {
    //pattern="row-1"前缀为row-1的行
    val prefixFilter = new PrefixFilter(Bytes.toBytes(pre))
    val scan = new Scan()
    scan.setFilter(prefixFilter)
    val proto = ProtobufUtil.toScan(scan)
    Base64.encodeBytes(proto.toByteArray)
  }

  /**
    * 后缀-过滤器
    *
    * @param pattern rowkey的后缀
    * @return 编码后的后缀
    */
  def getSufScanString(pattern: String): String = {
    //pattern=".*5"没有后缀过滤器，以正则表达式过滤，返回后缀为5的行
    val filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(pattern))
    val scan = new Scan()
    scan.setFilter(filter)
    val proto = ProtobufUtil.toScan(scan)
    Base64.encodeBytes(proto.toByteArray)
  }

  def close():Unit={
    if(conn!=null && !conn.isClosed){
      conn.close()
    }
  }
}
