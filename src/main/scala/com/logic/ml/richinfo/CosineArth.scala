package com.logic.ml.richinfo

import breeze.numerics.sqrt
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


/**
  *原理：多维空间两点与所设定的点形成夹角的余弦值。
  范围：[-1,1]，值越大，说明夹角越大，两点相距就越远，相似度就越小。
  余弦相似度模型：根据用户评分数据表，生成物品的相似矩阵；
  输入参数：user_rdd：用户行为表；
  输出参数：余弦相似矩阵：物品1，物品2，相似度值；
  */
object CosineArth {

  def consine ( user_item:RDD[(String,String,Double)] ) : (RDD[(String,String,Double)]) = {

    // 0 数据做准备
    val user_item_2=user_item.map(f => (f._1,(f._2,f._3))).sortByKey()
    user_item_2.cache
    //  1 (用户,物品,评分)笛卡尔积 (用户,物品,评分) =>（物品1,物品2,评分1,评分2）组合
    val item_item=user_item_2 join user_item_2
    val item_item_1=item_item.map(f=> ((f._2._1._1, f._2._2._1),(f._2._1._2, f._2._2._2)))
    println("--item_item_1------------------")
    item_item_1.take(100).foreach(println)
    //  2 （物品1,物品2,评分1,评分2）组合 => （物品1,物品2,评分1*评分2）组合并累加
    val item_grade = item_item_1.map(f=> (f._1,f._2._1*f._2._2 )).reduceByKey(_+_)
    println("--item_grade------------------")
    item_grade.take(100).foreach(println)
    //  3 对角矩阵
    val opposite=item_grade.filter(f=> f._1._1 == f._1._2)
    //  4 非对角矩阵
    val nonOpposite=item_grade.filter(f=> f._1._1 != f._1._2)
    //  5 计算相似度
    val cosine=nonOpposite.map(f=> (f._1._1, (f._1._1, f._1._2, f._2))).
      join(opposite.map(f=> (f._1._1, f._2)))
    val cosine_1=cosine.map(f=> (f._2._1._2, (f._2._1._1,
      f._2._1._2, f._2._1._3, f._2._2)))
    val cosine_2=cosine_1.join(opposite.map(f => (f._1._1, f._2)))
    val cosine_3 = cosine_2.map(f => (f._2._1._1,f._2._1._2,f._2._1._3,f._2._1._4,f._2._2))
    val result=cosine_3.map(f=> (f._1, f._2, (f._3 / sqrt(f._4 * f._5)) ))
    println("--result------------------")
    result.take(100).foreach(println)
    //  7 结果返回
    result

  }

  /**
    * 余弦夹角算法
    * @param oriRatings
    * @return
    */
  def consineSim(oriRatings:RDD[(String,String,Double)]  ) : RDD[(String, (String, Double))] ={
    //filter redundant (user,item,rating),this set user favorite (best-loved) 100 item
    val ratings = oriRatings.groupBy(k=>k._1).flatMap(x=>(x._2.toList.sortWith((x,y)=>x._3>y._3).take(100)))
    println("======ratings==================================================")
    ratings.take(10).foreach(println)

    // get num raters per movie, keyed on item id,,item2manyUser formating as (item,(user,item,rating))
    val item2manyUser = ratings.groupBy(tup => tup._2)
    println("======item2manyUser==================================================")
    item2manyUser.take(10).foreach(println)
    val numRatersPerItem = item2manyUser.map(grouped => (grouped._1, grouped._2.size))
    println("======numRatersPerItem==================================================")
    numRatersPerItem.take(10).foreach(println)
    // join ratings with num raters on item id,,ratingsWithSize formating as (user,item,rating,numRaters)
    val ratingsWithSize = item2manyUser.join(numRatersPerItem).
      flatMap(joined => {
        joined._2._1.map(f => (f._1, f._2, f._3, joined._2._2))
      })
    println("======ratingsWithSize==================================================")
    ratingsWithSize.take(10).foreach(println)
    // ratingsWithSize now contains the following fields: (user, item, rating, numRaters).

    // dummy copy of ratings for self join ,formating as ()
    val ratings2 = ratingsWithSize.keyBy(tup => tup._1)

    // join on userid and filter item pairs such that we don't double-count and exclude self-pairs

    //***计算半矩阵，减少计算量
    val ratingPairs =ratings2.join(ratings2).filter(f => f._2._1._2 < f._2._2._2)
    println("======ratingPairs==================================================")
    ratingPairs.take(10).foreach(println)
    // compute raw inputs to similarity metrics for each item pair

    val tempVectorCalcs =
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
      })
    println("======tempVectorCalcs==================================================")
    tempVectorCalcs.take(10).foreach(println)
    val vectorCalcs = tempVectorCalcs.groupByKey().map(data => {
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
    //.filter(x=>x._2._1>1)
    println("======vectorCalcs==================================================")
    vectorCalcs.take(10).foreach(println)
    val inverseVectorCalcs = vectorCalcs.map(x=>((x._1._2,x._1._1),(x._2._1,x._2._2,x._2._4,x._2._3,x._2._6,x._2._5,x._2._8,x._2._7)))
    println("======inverseVectorCalcs==================================================")
    inverseVectorCalcs.take(10).foreach(println)
    val vectorCalcsTotal = vectorCalcs ++ inverseVectorCalcs
    println("======vectorCalcsTotal==================================================")
    vectorCalcsTotal.take(10).foreach(println)
    // compute similarity metrics for each item pair
    // modify formula as : cosSim *size/(numRaters*math.log10(numRaters2+10))
    val tempSimilarities =
      vectorCalcsTotal.map(fields => {
        val key = fields._1
        val (size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq, numRaters, numRaters2) = fields._2
        val cosSim = cosineSimilarity(dotProduct, scala.math.sqrt(ratingNormSq), scala.math.sqrt(rating2NormSq))*size/(numRaters*math.log10(numRaters2+10))
        (key._1,(key._2, cosSim))
      })
    println("======tempSimilarities==================================================")
    tempSimilarities.take(10).foreach(println)
    val similarities = tempSimilarities.groupByKey().flatMap(x=>{
      x._2.map(temp=>(x._1,(temp._1,temp._2))).toList.sortWith((a,b)=>a._2._2>b._2._2).take(50)
    })
    println("======similarities==================================================")
    similarities.take(10).foreach(println)
    similarities

  }


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



