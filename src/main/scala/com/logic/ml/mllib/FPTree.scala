package com.logic.ml.mllib

import org.apache.spark.mllib.fpm.FPGrowth
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object FPTree {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setAppName("fp-tree")
    val sc=new SparkContext(conf)
    val data=sc.textFile("D:/spark-branch-1.5/data/mllib/sample_fpgrowth.txt")
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
    val dssData=sc.textFile("D:/data/dss")
    println(dssData.count())
    val transactions=data.map(_.trim.split(" "))

    val fpg=new FPGrowth().setMinSupport(0.2).setNumPartitions(10)
    val model=fpg.run(transactions)

    //遍历频繁项集
    model.freqItemsets.collect().foreach { itemset =>
      println(itemset.items.mkString("[", ",", "]") + ", " + itemset.freq)
    }

    //设定最小置信度
    val minConfidence = 0.8

    //产生关联规则
    model.generateAssociationRules(minConfidence).collect().foreach { rule =>
      println(
        rule.antecedent.mkString("[", ",", "]")
          + " => " + rule.consequent .mkString("[", ",", "]")
          + ", " + rule.confidence)
    }

  }

}
