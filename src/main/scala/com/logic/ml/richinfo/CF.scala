package com.logic.ml.richinfo

import com.logic.ml.richinfo.evaluation.Evaluation
import org.apache.spark.mllib.evaluation.RegressionMetrics
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
//    val source: RDD[Option[Rating]] = sc.textFile("d:/part-00001")
//      .map(line => {
//        val lines = line.split("&")
//        try {
//          Some(Rating(lines(0).toInt, lines(1).toInt, 1.0.toFloat))
//        } catch {
//          case _: Throwable => None
//        }
//      })
    val source=getSparkALSData(sc)
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
    }).cache()
      .randomSplit(Array(0.6, 0.4), 45423L)
    val model = ALS.train(data(0).cache(), 10, 20)
    printMSE(data(1), model)
    val pros = model.recommendProductsForUsers(10).cache()
    val ceshi = data(1).map(rating => {
      (rating.user.toString, rating.product.toString)
    }).cache()
    val pre = pros.map(user => {
      (user._1.toString, user._2.map(_.product.toString).toIterable)
    }).cache()

    val eva = new Evaluation(ceshi, pre, 2000)
    println(eva)
    val ceshi2 = data(0).map(rating => {
      (rating.user.toString, rating.product.toString)
    }).cache()
    val eva2=new Evaluation(ceshi2,pre,2000)
    println(eva2)
  }
  def getSparkALSData(sc:SparkContext): RDD[Option[Rating]] ={
    val data=sc.textFile("D:/spark-branch-1.5/data/mllib/als/sample_movielens_ratings.txt").map(line=>{
      val datas=line.split("::")
      try{
        Some(Rating(datas(0).toInt,datas(1).toInt,datas(2).toDouble))
      }catch {
        case _: Throwable =>None
      }
    })
    data
  }

  def printMSE(ratings: RDD[Rating], model: MatrixFactorizationModel) = {
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

    println("metrics.meanSquaredError=" + metrics.meanSquaredError
      + ",metrics.rootMeanSquaredError=" + metrics.rootMeanSquaredError
      + ",metrics.meanAbsoluteError=" + metrics.meanAbsoluteError)
  }
}
