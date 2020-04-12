package com.logic.bigdata.spark.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Wang Guodong on 2018/3/11.
  */
object RDDOperating {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("test")
    val sc=new SparkContext(conf)

    val rdd=sc.parallelize(Seq(
      (1,"wang"),
      (1,"wu"),
      (1,"feng"),
      (2,"li"),
      (2,"zhu"),
      (3,"yang")
    ),3)
    def create(str:String):String={
      "<"+str+">"
    }
    def me(str1:String,str2:String):String={
      str1+"-"+str2
    }
    def me2(str1:String,str2:String):String={
      str1+"="+str2
    }
    val r2=rdd.combineByKey(create,me,me2)
    rdd.groupByKey()
//    rdd.aggregateByKey()
    rdd.reduceByKey(_+_,100)
    println(r2.collect().toList)
  }

}
