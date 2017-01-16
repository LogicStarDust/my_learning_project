package com.logic.ml.richinfo

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object CollaborativeFilterItemBased_modify {
  def main(args: Array[String]) {
    /**
      * Parameters to regularize correlation.
      */
    val PRIOR_COUNT = 10
    val PRIOR_CORRELATION = 0

    val sparkConf = new SparkConf().setAppName("item-based cf")
    val sc = new SparkContext(sparkConf)

    // extract (userid, movieid, rating) from ratings data
    val ratings = sc.textFile("/path/to/input").map(line => {
      val fields = line.split("\t")
      (fields(0).toInt, fields(1).toInt, fields(2).toInt)
    })

    // get num raters per movie, keyed on item id
    val item2manyUser = ratings.groupBy(tup => tup._2)

    val numRatersPerItem = item2manyUser.map(grouped => (grouped._1, grouped._2.size))

    // join ratings with num raters on item id
    val ratingsWithSize = item2manyUser.join(numRatersPerItem).
      flatMap(joined => {
        joined._2._1.map(f => (f._1, f._2, f._3, joined._2._2))
      })
    // ratingsWithSize now contains the following fields: (user, item, rating, numRaters).

    // dummy copy of ratings for self join
    val ratings2 = ratingsWithSize.keyBy(tup => tup._1)

    // join on userid and filter item pairs such that we don‘t double-count and exclude self-pairs

    val ratingPairs =ratings2.join(ratings2).filter(f => f._2._1._2 < f._2._2._2)

    // compute raw inputs to similarity metrics for each item pair
    val vectorCalcs =
      ratingPairs.map(data => {
        val key = (data._2._1._2, data._2._2._2)
        val stats =
          (data._2._1._3 * data._2._2._3, // rating 1 * rating 2
            data._2._1._3,                // rating item 1
            data._2._2._3,                // rating item 2
            math.pow(data._2._1._3, 2),   // square of rating item 1
            math.pow(data._2._2._3, 2),   // square of rating item 2
            data._2._1._4,                // number of raters item 1
            data._2._2._4)                // number of raters item 2
        (key, stats)
      }).groupByKey().map(data => {
        val key = data._1
        val vals = data._2
        val size = vals.size
        val dotProduct = vals.map(f => f._1).sum
        val ratingSum = vals.map(f => f._2).sum
        val rating2Sum = vals.map(f => f._3).sum
        val ratingSq = vals.map(f => f._4).sum
        val rating2Sq = vals.map(f => f._5).sum
        val numRaters = vals.map(f => f._6).max
        val numRaters2 = vals.map(f => f._7).max
        (key, (size, dotProduct, ratingSum, rating2Sum, ratingSq, rating2Sq, numRaters, numRaters2))
      })

    // compute similarity metrics for each item pair
    val similarities =
      vectorCalcs.map(fields => {
        val key = fields._1
        val (size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq, numRaters, numRaters2) = fields._2
        //        val corr = correlation(size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq)
        //        val regCorr = regularizedCorrelation(size, dotProduct, ratingSum, rating2Sum,
        //          ratingNormSq, rating2NormSq, PRIOR_COUNT, PRIOR_CORRELATION)
        val cosSim = cosineSimilarity(dotProduct, scala.math.sqrt(ratingNormSq), scala.math.sqrt(rating2NormSq))
        //        val jaccard = jaccardSimilarity(size, numRaters, numRaters2)

        (key._1,(key._2, cosSim))
      })

    val inverseSim = similarities.map(ori=>(ori._2._1,(ori._1,ori._2._2)))
    val simTotal = inverseSim ++ similarities
    val cutNumSim = simTotal.groupByKey().map(sim=>(sim._1,sim._2.toList.sortBy(x=>x._2).take(50)))


    val ratingsInverse = ratings.map(rating=>(rating._2,(rating._1,rating._3)))
    val userRecommend = ratingsInverse.join(cutNumSim).flatMap(obj=>obj._2._2.map(x=>((obj._2._1._1,x._1),obj._2._1._2*x._2)))
    val filterItem = ratings.map(x=>((x._1,x._2),Double.NaN))
    val totalScore = userRecommend ++ filterItem

    val finalResult = totalScore.reduceByKey(_+_).filter(x=> !(x._2 equals(Double.NaN))).
      map(x=>(x._1._1,x._1._2,x._2)).groupBy(x=>x._1).flatMap(x=>(x._2.toList.sortBy(o=>o._3).take(50)))
    finalResult.saveAsTextFile("/path/to/savefile")
  }

  // *************************
  // * SIMILARITY MEASURES
  // *************************

  /**
    * The correlation between two vectors A, B is
    *   cov(A, B) / (stdDev(A) * stdDev(B))
    *
    * This is equivalent to
    *   [n * dotProduct(A, B) - sum(A) * sum(B)] /
    *     sqrt{ [n * norm(A)^2 - sum(A)^2] [n * norm(B)^2 - sum(B)^2] }
    */
  def correlation(size : Double, dotProduct : Double, ratingSum : Double,
                  rating2Sum : Double, ratingNormSq : Double, rating2NormSq : Double) = {

    val numerator = size * dotProduct - ratingSum * rating2Sum
    val denominator = scala.math.sqrt(size * ratingNormSq - ratingSum * ratingSum) *
      scala.math.sqrt(size * rating2NormSq - rating2Sum * rating2Sum)

    numerator / denominator
  }

  /**
    * Regularize correlation by adding virtual pseudocounts over a prior:
    *   RegularizedCorrelation = w * ActualCorrelation + (1 - w) * PriorCorrelation
    * where w = # actualPairs / (# actualPairs + # virtualPairs).
    */
  def regularizedCorrelation(size : Double, dotProduct : Double, ratingSum : Double,
                             rating2Sum : Double, ratingNormSq : Double, rating2NormSq : Double,
                             virtualCount : Double, priorCorrelation : Double) = {

    val unregularizedCorrelation = correlation(size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq)
    val w = size / (size + virtualCount)

    w * unregularizedCorrelation + (1 - w) * priorCorrelation
  }

  /**
    * The cosine similarity between two vectors A, B is
    *   dotProduct(A, B) / (norm(A) * norm(B))
    */
  def cosineSimilarity(dotProduct : Double, ratingNorm : Double, rating2Norm : Double) = {
    dotProduct / (ratingNorm * rating2Norm)
  }

  /**
    * The Jaccard Similarity between two sets A, B is
    *   |Intersection(A, B)| / |Union(A, B)|
    */
  def jaccardSimilarity(usersInCommon : Double, totalUsers1 : Double, totalUsers2 : Double) = {
    val union = totalUsers1 + totalUsers2 - usersInCommon
    usersInCommon / union
  }
}
