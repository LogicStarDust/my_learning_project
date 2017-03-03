package com.logic.ml.richinfo


import java.sql.{DriverManager, PreparedStatement}

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object TestMysql {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setAppName("TestApp")
    val sc=new SparkContext(conf)
    val data=sc.parallelize(Seq(1,2,3,4,5,8,987,6565,4564,3234,6574),5)

    println(data.getNumPartitions)

    data.foreachPartition(partition=>{
      var ps: PreparedStatement = null
      val sql ="update test_mysql set name=? where id=?"

      Class.forName("com.mysql.jdbc.Driver")
      val conn = DriverManager.getConnection("jdbc:mysql://192.168.1.105:3306/ml_ex_ceshi?user=ceshi&password=ceshi")
      val name=math.random.toString
      partition.foreach(e=>{
        ps=conn.prepareStatement(sql)
        math.random
        ps.setString(1,e+"-"+name)
        ps.setInt(2,e)
        ps.executeUpdate()
        val s2=ps.executeUpdate()
        println("插入数据库完成，状态："+s2)
      })

    })
  }
}
