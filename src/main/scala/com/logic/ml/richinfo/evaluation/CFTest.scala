package com.logic.ml.richinfo.evaluation

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object CFTest {
  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setAppName("CF")
//    val sc = new SparkContext(conf)
//    sc.setCheckpointDir("d:/checkpoint")
//    val sourc= sc.textFile("f://hitRecUserNumAndRecList").map(line=>{
//
//    })
//    println(sourc.count())
//    sourc.take(10).foreach(println)
    val l1=List("a","b","c")
    val l2=List("a","b","d")
    println(l1 union l2)
  }

}
