package com.logic.ml.richinfo.evaluation

import org.apache.spark.rdd.RDD

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */

/**
  * 评估一个数据集合上推荐算法的准确率，召回率，覆盖度和流行度
  *
  */
class Evaluation(data: RDD[(String, String)], pre: RDD[(String, Iterable[String])], allItemSize: Int) extends Serializable {

  //把测试集按用户分组，提取成（用户，商品List）的形式
  private val dataUserGroup = data.groupBy(_._1).map(user => {
    val userList = user._2.map(u => {
      u._2
    }).toList
    (user._1, userList)
  }).cache()
  //组合测试集合与预测集合
  private val userGroup = dataUserGroup.join(pre).cache()

  //计算全体的命中个数、推荐个数、点击个数和推荐物品集合
  private val hitRecUserNumAndRecList = userGroup.map(user => {
    val recList = user._2._2.toList
    val userList = user._2._1
    val hitList = recList intersect userList
    (hitList.size, recList.size, userList.size, recList)
  }).reduce((a, b) => {
    val hitNum = a._1 + b._1
    val recNum = a._2 + b._2
    val clickNum = a._3 + b._3
    val recList = a._4 union b._4
    (hitNum, recNum, clickNum, recList.distinct)
  })
  //计算每个商品的流行度
  private val itemGroup = data.groupBy(_._2).map(item => {
    math.log10(1 + item._2.size)
  }).cache()

  var recall: Double = hitRecUserNumAndRecList._1.toDouble / hitRecUserNumAndRecList._3.toDouble
  val precision: Double = hitRecUserNumAndRecList._1.toDouble / hitRecUserNumAndRecList._2.toDouble
  val f1: Double = recall * precision * 2 / (recall + precision)
  val coverage: Double = hitRecUserNumAndRecList._4.size.toDouble / allItemSize.toDouble
  val averagePopular: Double = itemGroup.sum() / itemGroup.count()

  override def toString: String = {
    "hitNum="+hitRecUserNumAndRecList._1+
    ",recNum="+hitRecUserNumAndRecList._2+
    ",clickNum="+hitRecUserNumAndRecList._3+
    ",recall=" + recall +
      ",precision=" + precision +
      ",f1=" + f1 +
      ",coverage=" + coverage +
      ",averagePopular=" + averagePopular
  }
}
