package com.logic.util.db


import java.util.LinkedList
import java.sql.DriverManager
import java.sql.Connection

import com.logic.util.ResourcesUtil
import com.logic.util.content.ConfigContent

/**
  * 数据库连接池工具类
  * auth:刘宪领
  */
object JdbcPool {

  private val max_connection = ResourcesUtil.getVal(ConfigContent.MAX_CONNECTION) //连接池总数
  private val connection_num = ResourcesUtil.getVal(ConfigContent.CONNECTION) //产生连接数
  private var current_num = 0 //当前连接池已产生的连接数
  private val pools = new LinkedList[Connection]() //连接池
  private val driver = ResourcesUtil.getVal(ConfigContent.DRIVER)
  private val url = ResourcesUtil.getVal(ConfigContent.JDBC_URL)
  private val username = ResourcesUtil.getVal(ConfigContent.USERNAME)
  private val password = ResourcesUtil.getVal(ConfigContent.PASSWORD)
  /**
    * 获得连接
    */
  def getConn():Connection={
    initConnectionPool()
    pools.poll()
  }
  /**
    * 释放连接
    */
  def releaseCon(con:Connection){
    pools.push(con)
  }

  def main(args: Array[String]) {
    getConn()
  }
  /**
    * 加载驱动
    */
  private def before() {
    if (current_num > max_connection.toInt && pools.isEmpty()) {
      Thread.sleep(2000)
      before()
    } else {
      Class.forName(driver)
    }
  }
  /**
    * 获得连接
    */
  private def initConn(): Connection = {
    val conn = DriverManager.getConnection(url, username, password)
    conn
  }
  /**
    * 初始化连接池
    */
  private def initConnectionPool(): LinkedList[Connection] = {
    AnyRef.synchronized({
      if (pools.isEmpty()) {
        before()
        for (i <- 1 to connection_num.toInt) {
          pools.push(initConn())
          current_num += 1
        }
      }
      pools
    })
  }

}