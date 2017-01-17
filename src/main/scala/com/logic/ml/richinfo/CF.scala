package com.logic.ml.richinfo

import org.apache.spark.mllib.evaluation.{BinaryClassificationMetrics, MulticlassMetrics, RankingMetrics, RegressionMetrics}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object CF {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("CF")
    val sc = new SparkContext(conf)
    val source: RDD[Option[Rating]] = sc.textFile("d:/part-00000")
      .map(line => {
        val lines = line.split("&")
        try {
          Some(Rating(lines(0).toInt, lines(1).toInt, 1.0.toFloat))
        } catch {
          case _: Throwable => None
        }
      })
    val data = source.filter({
      case None => false
      case Some(_) => true
    })
      .map(line => {
        line.get
      })
      .map(rating => {
        ((rating.user, rating.product), rating.rating)
      })
      .reduceByKey(_ + _).map(line => {
      Rating(line._1._1, line._1._2, line._2)
      })
      .randomSplit(Array(0.6, 0.4), 45423L)

    val model = ALS.train(data(0), 10, 30)
    printMSE(data(0), model)
    printMSE(data(1), model)
  }

  def printMSE(ratings: RDD[Rating], model: MatrixFactorizationModel)= {
    //计算MSE
    val usersProducts = ratings.map(rating => (rating.user, rating.product))

    val predictions = model.predict(usersProducts)
      .map(rating => {
        ((rating.user, rating.product), rating.rating)
      })

    val ratesAndPreds = ratings.map(rating => {
      ((rating.user, rating.product), rating.rating)
    })
    val joins = ratesAndPreds.join(predictions)

    val metrics = new RegressionMetrics(joins.map(_._2))

    println("metrics.meanSquaredError="+metrics.meanSquaredError
      +",metrics.rootMeanSquaredError="+metrics.rootMeanSquaredError
      +",metrics.meanAbsoluteError="+metrics.meanAbsoluteError)
  }
}
