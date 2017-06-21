package com.logic.ml.mllib

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.random.RandomRDDs._


/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object MllibeDataStructure {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setAppName("test").setMaster("local")
    val sc=new SparkContext(conf)
    val u=normalRDD(sc,1000L,10).map(x=>1.0+x*2.0)
    u.take(10).foreach(println)
  }

}
