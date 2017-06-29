package com.logic.ml.mllib

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object FPGrowthOfDSS {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("FPGrowthOfDSS")
    val sqlContext = new SQLContext(new SparkContext(conf))
    val dss = getDSSData(sqlContext)
    dss.show()
  }


  def getDSSData(sqlContext: SQLContext): DataFrame = {
    val dssRowData = sqlContext.sparkContext.textFile("D:/data/dss").map(line => {
      val lines = blankDistinct(line.trim).split(" ")
      Row(
        lines(0), lines(1), lines(2), lines(3), lines(4),
        lines(5), lines(6), lines(7), lines(8), lines(9),
        lines(10), lines(11), lines(12), lines(13)
      )
    })
    /*
      缺测和空白无记录一律用32744填补
       站号    经    纬    年      月    日 沙暴代码  开始时间   结束时间 能见度  十分钟平均最大风速  风向(16方位制)  极大风速(0.1m/s) 风向(16方位制)
      50353 12639  5143  1957     6    11   231     1923      1925     8       32744           32744         32744           32744
      50603 11649  4840  2007     4    29   231     1834      1847    90        117             15            205               1
      50915 11658  4531  2007     3    30   231     1840      2000     8        120             12            170               13
      50915 11658  4531  2007     3    31   231     2000      2108     8         96             12            148               12
      50924 11940  4532  2007     6    10   231     1728      1738   110        113             11            186               11
      50924 11940  4532  2007     6    11   231     1505      1513    80        123             10            209               11
       */
    //schema
    val SOURCE_SCHEMA =
    StructType(
      Seq(
        StructField("station", StringType, nullable = true),
        StructField("longitude", StringType, nullable = true),
        StructField("latitude", StringType, nullable = true),
        StructField("year", StringType, nullable = true),
        StructField("month", StringType, nullable = true),
        StructField("day", StringType, nullable = true),
        StructField("code", StringType, nullable = true),
        StructField("start_time", StringType, nullable = true),
        StructField("end_time", StringType, nullable = true),
        StructField("visibility", StringType, nullable = true),
        StructField("wind_speed_avg", StringType, nullable = true),
        StructField("orientation_avg", StringType, nullable = true),
        StructField("wind_speed_max", StringType, nullable = true),
        StructField("orientation_max", StringType, nullable = true)
      )
    )
    sqlContext.createDataFrame(dssRowData, SOURCE_SCHEMA)
  }

  def blankDistinct(str: String): String = {
    val cs = str.toArray
    cs.foldLeft[String]("")((s, c) => {
      if (c == ' ' && s.nonEmpty && s.last == ' ') s
      else s + c
    })
  }
}
