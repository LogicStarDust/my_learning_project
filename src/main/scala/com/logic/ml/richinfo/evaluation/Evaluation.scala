package com.logic.ml.richinfo.evaluation

import org.apache.spark.rdd.RDD

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */

/**
  * 评估一个数据集合上推荐算法的准确率，召回率，覆盖度和流行度
  * @param data 数据集合 (用户id,商品id,评分)
  * @param f  推荐方法,传入用户id，给出推荐商品的列表
  * @param allItemSize  所有商品的数量
  */
class Evaluation(data: RDD[(String, String, Double)], f: String => Iterable[String], allItemSize: Int) extends Serializable{

  private val userGroup = data.groupBy(_._1)
  private val hitRecUserNumAndRecList = userGroup.map(user => {
    val recList = f(user._1).toList
    val userList = user._2.map(_._2).toList
    val hitList = recList intersect userList
    (hitList.size, recList.size, userList.size, recList)
  }).reduce((a, b) => {
    (a._1 + b._1, a._2 + b._2, a._3 + b._3, a._4 union b._4)
  })
  private val itemGroup = data.groupBy(_._2).map(item => {
    math.log10(1 + item._2.size)
  })

  var recall: Double = hitRecUserNumAndRecList._1.toDouble / hitRecUserNumAndRecList._2.toDouble
  val precision: Double = hitRecUserNumAndRecList._1.toDouble / hitRecUserNumAndRecList._3.toDouble
  val f1:Double=recall*precision*2/(recall+precision)
  val coverage: Double = hitRecUserNumAndRecList._4.size.toDouble / allItemSize.toDouble
  val averagePopular: Double = itemGroup.sum() / itemGroup.count()

  override def toString: String = {
    "recall="+recall+",precision="+precision+",f1="+f1+",coverage="+coverage+",averagePopular="+averagePopular
  }
}
