package com.logic.ml.richinfo

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object TestRDD {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("rddJoin").setMaster("local")
    val sc = new SparkContext(conf)

    val rdd1 = sc.parallelize(Array((1, 21), (2, 42), (3, 41),(4,42)), 1)
    val rdd2 = sc.parallelize(Array((3, 4), (4, 41)), 1)
    println("rdd1=")
    rdd1.foreach(println)
    println("rdd2=")
    rdd2.foreach(println)

    val rdd3 = rdd1.join(rdd2)
    println("rdd1.join(rdd2)=")
    rdd3.foreach(println)
    println("rdd1.zipWithIndex=")
    rdd1.zipWithIndex.foreach(println)
  }

}
