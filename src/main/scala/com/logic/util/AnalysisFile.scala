package com.logic.util

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.recommendation.Rating

/**
  * 解析数组字段
  */
object AnalysisFile {
  /**
    * 全部行为数据解析
    * rawData 原始数据集合 原始数据集合
    * @param splitSign 分隔符
    * @return
    */
  def allBehav(
                rawData:RDD[String],
                splitSign:String
              ): RDD[(String,String,Double)] = {

    val lines =rawData.map(_.split(splitSign).take(5))
    
    val item = lines.map( arr=>{
      if(arr.length<5)println(arr.toList)
      (arr(0), arr(1),arr(4).toDouble)
    })
    item
  }

  /**
    * 全部行为数据解析
    * rawData 原始数据集合
    * @param splitSign 分隔符
    * @return
    */
  def allBehavTag(
                rawData:RDD[String],
                splitSign:String
              ): RDD[(String,String,Double)] = {

    val lines =rawData.map(_.split(splitSign).take(9))

    val item = lines.map( arr=>{
      if(arr.length<5)println(arr.toList)
      (arr(6),arr(1),arr(4).toDouble)
    }).filter(_._1.nonEmpty)
    item
  }
  /**
    * 基于内容的行为数据文件解析
    * rawData 原始数据集合
    * @param splitSign 分隔符
    * @return
    */
  def conBehav(
                    rawData:RDD[String],
                    splitSign:String
                  ): RDD[(String,String,Double)] = {

    val lines =rawData.map(_.split(splitSign))
    val item = lines.map( arr=>{
      (arr(0), arr(1),1.0)
    }).sortBy(_._1)
    item
  }
  
    /**
    * 解析为rating格式
    * rawData 原始数据集合
    * @param splitSign 分隔符
    * @return
    */
  def ratingBehav(
                    rawData:RDD[String],
                    splitSign:String
                  ): RDD[Rating] = {

    val lines =rawData.map(_.split(splitSign))
    val item = lines.map( arr=>{
      Rating(arr(0).toInt,arr(1).toInt,1.0)
    })
    item
  }
  
   /**
    * 解析为rating格式
    * rawData 原始数据集合
    * @param splitSign 分隔符
    * @return
    */
  def itemIdBehav(
                    rawData:RDD[String],
                    splitSign:String
                  ): RDD[Int] = {

    val lines =rawData.map(_.split(splitSign))
    val item = lines.map( arr=>{
      arr(1).toInt
    }).distinct()
    item
  }
}
