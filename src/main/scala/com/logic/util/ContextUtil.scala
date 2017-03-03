package com.logic.util

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 初始化Context类
  * user:刘宪领
  */
object ContextUtil {
  /**
    * 初始化Context
    *
    * @return
    */
  def getCtx(appName:String):SparkContext={
    val conf = new SparkConf().setAppName(appName)
    val sc = new SparkContext(conf)
    sc
  }

  /**
    * 获取spark执行sql的上下文
    *
    * @return
    */
  def getSQLCtx(sc: SparkContext): SQLContext = {
    new SQLContext(sc)
  }
}
