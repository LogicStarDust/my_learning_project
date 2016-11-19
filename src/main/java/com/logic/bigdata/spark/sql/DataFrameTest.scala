package com.logic.bigdata.spark.sql

import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object DataFrameTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("ML Shop Order By Scala")
    val sparkContext = new SparkContext(conf)
    val sqlContext = new SQLContext(sparkContext)

    val rdd = sparkContext.parallelize(Seq(Row("wang","22"), Row("guo","21"), Row("dong","13")))
    val schameString = "a b"
    val schame = StructType(
      schameString.split(" ").map(x => StructField(x, StringType, true))
    )
    val df = sqlContext.createDataFrame(rdd, schame)
    df.show()
  }

}
