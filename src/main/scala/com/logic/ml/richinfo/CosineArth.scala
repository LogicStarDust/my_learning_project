package com.logic.ml.richinfo

import breeze.numerics.sqrt
import org.apache.spark.rdd.RDD


/**
  * 原理：多维空间两点与所设定的点形成夹角的余弦值。
  * 范围：[-1,1]，值越大，说明夹角越大，两点相距就越远，相似度就越小。
  * 余弦相似度模型：根据用户评分数据表，生成物品的相似矩阵；
  * 输入参数：user_rdd：用户行为表；
  * 输出参数：余弦相似矩阵：物品1，物品2，相似度值；
  */
object CosineArth {

  def consine(user_item: RDD[(String, String, Double)]): (RDD[(String, String, Double)]) = {

    // 0 数据做准备
    val user_item_2 = user_item.map(f => (f._1, (f._2, f._3))).sortByKey()
    user_item_2.cache
    //  1 (用户,物品,评分)笛卡尔积 (用户,物品,评分) =>(物品1,物品2,评分1,评分2)组合
    val item_item = user_item_2 join user_item_2
    val item_item_1 = item_item.map(f => ((f._2._1._1, f._2._2._1), (f._2._1._2, f._2._2._2)))
    println("--item_item_1------------------")
    item_item_1.take(100).foreach(println)
    //  2 (物品1,物品2,评分1,评分2)组合 => (物品1,物品2,评分1*评分2)组合并累加
    val item_grade = item_item_1.map(f => (f._1, f._2._1 * f._2._2)).reduceByKey(_ + _)
    println("--item_grade------------------")
    item_grade.take(100).foreach(println)
    //  3 对角矩阵
    val opposite = item_grade.filter(f => f._1._1 == f._1._2)
    //  4 非对角矩阵
    val nonOpposite = item_grade.filter(f => f._1._1 != f._1._2)
    //  5 计算相似度
    val cosine = nonOpposite.map(f => (f._1._1, (f._1._1, f._1._2, f._2))).
      join(opposite.map(f => (f._1._1, f._2)))
    val cosine_1 = cosine.map(f => (f._2._1._2, (f._2._1._1,
      f._2._1._2, f._2._1._3, f._2._2)))
    val cosine_2 = cosine_1.join(opposite.map(f => (f._1._1, f._2)))
    val cosine_3 = cosine_2.map(f => (f._2._1._1, f._2._1._2, f._2._1._3, f._2._1._4, f._2._2))
    val result = cosine_3.map(f => (f._1, f._2, (f._3 / sqrt(f._4 * f._5))))
    println("--result------------------")
    result.take(100).foreach(println)
    //  7 结果返回
    result

  }

  /**
    * 余弦夹角算法
    *
    * @param oriRatings
    * @return
    */
  def consineSim(oriRatings: RDD[(String, String, Double)]): RDD[(String, (String, Double))] = {

    //过滤冗余(用户、项目评级),这组用户最喜欢的(最喜欢的)100项.通过用户分组，分组后对每个用户打分商品排序，取每个用户前100个
    val ratings = oriRatings.groupBy(k => k._1).flatMap(x => (x._2.toList.sortWith((x, y) => x._3 > y._3).take(100)))
    println("======ratings==================================================")
    ratings.take(10).foreach(println)

    //通过商品分组
    val item2manyUser = ratings.groupBy(tup => tup._2)
    println("======item2manyUser==================================================")
    item2manyUser.take(10).foreach(println)
    //统计每个商品的喜欢人群的数量
    val numRatersPerItem = item2manyUser.map(grouped => (grouped._1, grouped._2.size))
    println("======numRatersPerItem==================================================")
    numRatersPerItem.take(10).foreach(println)
    // join ratings with num raters on item id,,ratingsWithSize formating as (user,item,rating,numRaters)
    //把每个商品的喜欢人数附加到商品分组表中，然后打开每个商品的人群集合，形成(用户，商品，评分，喜欢本商品的用户数)
    val ratingsWithSize = item2manyUser.join(numRatersPerItem).
      flatMap(joined => {
        joined._2._1.map(f => (f._1, f._2, f._3, joined._2._2))
      })
    println("======ratingsWithSize==================================================")
    ratingsWithSize.take(10).foreach(println)

    //前用户id作为key
    val ratings2 = ratingsWithSize.keyBy(tup => tup._1)


    //***计算半矩阵，减少计算量
    //把数据(用户id，商品id，评分，喜欢的用户数)通过用户id进行自连接，保留商品id小于商品id的数据最终形成(用户id,(用户id，商品id，评分，喜欢的用户数),(用户id，商品id，评分，喜欢的用户数))
    val ratingPairs = ratings2.join(ratings2).filter(f => f._2._1._2 < f._2._2._2)
    println("======ratingPairs==================================================")
    ratingPairs.take(10).foreach(println)
    // compute raw inputs to similarity metrics for each item pair

    // 计算((商品ID，商品ID),(评分相乘,商品1的评分,商品2的评分,商品1的评分的平方,商品2的评分的平方,喜欢本商品1用户数,喜欢本商品2用户数))
    //key(商品ID，商品ID)有重复
    val tempVectorCalcs =
    ratingPairs.map(data => {
      //key保留两个商品(这时候用户同一个)
      val key = (data._2._1._2, data._2._2._2)
      val stats =
        (data._2._1._3 * data._2._2._3, // rating 1 * rating 2 评分相乘
          data._2._1._3, // rating item 1  商品1的评分
          data._2._2._3, // rating item 2  商品2的评分
          math.pow(data._2._1._3, 2), // square of rating item 1  商品1的评分的平方
          math.pow(data._2._2._3, 2), // square of rating item 2  商品2的评分的平方
          data._2._1._4, // number of raters item 1  喜欢本商品1用户数
          data._2._2._4) // number of raters item 2  喜欢本商品2用户数
      (key, stats)
    })
    println("======tempVectorCalcs==================================================")
    tempVectorCalcs.take(10).foreach(println)

    //根据(商品ID，商品ID)聚合数据。
    // ((商品ID，商品ID),(同时喜欢两个商品的用户数,评分相乘,商品1的评分,商品2的评分,商品1的评分的平方,商品2的评分的平方,喜欢本商品1用户数,喜欢本商品2用户数))
    //key(商品ID，商品ID)无重复
    val vectorCalcs = tempVectorCalcs.groupByKey().map(data => {
      //商品id对
      val key = data._1
      //(评分相乘,商品1的评分,商品2的评分,商品1的评分的平方,商品2的评分的平方,喜欢本商品1用户数,喜欢本商品2用户数))
      val vals = data._2
      val size = vals.size
      //(评分相乘,商品1的评分,商品2的评分,商品1的评分的平方,商品2的评分的平方)这些字段求和
      val dotProduct = vals.map(f => f._1).sum
      val ratingSum = vals.map(f => f._2).sum
      val rating2Sum = vals.map(f => f._3).sum
      val ratingSq = vals.map(f => f._4).sum
      val rating2Sq = vals.map(f => f._5).sum
      //喜欢商品1的最大值
      val numRaters = vals.map(f => f._6).max
      //喜欢商品2的最大值
      val numRaters2 = vals.map(f => f._7).max
      (key, (size, dotProduct, ratingSum, rating2Sum, ratingSq, rating2Sq, numRaters, numRaters2))
    })
    //.filter(x=>x._2._1>1)
    println("======vectorCalcs==================================================")
    vectorCalcs.take(10).foreach(println)

    //交换两个商品的前后,注意后面的评分等属性也位置对换
    val inverseVectorCalcs = vectorCalcs.map(x => ((x._1._2, x._1._1), (x._2._1, x._2._2, x._2._4, x._2._3, x._2._6, x._2._5, x._2._8, x._2._7)))
    println("======inverseVectorCalcs==================================================")
    inverseVectorCalcs.take(10).foreach(println)

    //合并交换和为交换的数据为一个总体（两个半矩阵合并全矩阵）
    val vectorCalcsTotal = vectorCalcs ++ inverseVectorCalcs
    println("======vectorCalcsTotal==================================================")
    vectorCalcsTotal.take(10).foreach(println)
    // compute similarity metrics for each item pair
    // modify formula as : cosSim *size/(numRaters*math.log10(numRaters2+10))

    //
    val tempSimilarities =
      vectorCalcsTotal.map(fields => {
        val key = fields._1
        //(同时喜欢两个商品的用户数,评分相乘,商品1的评分,商品2的评分,商品1的评分的平方,商品2的评分的平方,喜欢本商品1用户数,喜欢本商品2用户数))
        val (size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq, numRaters, numRaters2) = fields._2
        //通过cosineSimilarity计算余弦相似度传入（评分相乘的积，商品1的评分的平方的根，,商品2的评分的平方的根），然后余弦相似度乘以同时喜欢两个商品的数量
        //最后除以商品1的用户数和 商品2的用户数加上10后的和的10对数
        val cosSim = cosineSimilarity(dotProduct, scala.math.sqrt(ratingNormSq), scala.math.sqrt(rating2NormSq)) * size / (numRaters * math.log10(numRaters2 + 10))
        //(商品1的id,(商品2的id，余弦相似度))
        (key._1, (key._2, cosSim))
      })
    println("======tempSimilarities==================================================")
    tempSimilarities.take(10).foreach(println)

    //留存每件商品相似前50的商品
    val similarities = tempSimilarities.groupByKey().flatMap(x => {
      x._2.map(
        temp => (x._1, (temp._1, temp._2))
      )
        .toList.sortWith(
        (a, b) => a._2._2 > b._2._2
      )
        .take(50)
    })
    println("======similarities==================================================")
    similarities.take(10).foreach(println)
    similarities

  }


  /**
    * The correlation between two vectors A, B is
    * cov(A, B) / (stdDev(A) * stdDev(B))
    *
    * This is equivalent to
    * [n * dotProduct(A, B) - sum(A) * sum(B)] /
    * sqrt{ [n * norm(A)^2 - sum(A)^2] [n * norm(B)^2 - sum(B)^2] }
    */
  def correlation(size: Double, dotProduct: Double, ratingSum: Double,
                  rating2Sum: Double, ratingNormSq: Double, rating2NormSq: Double) = {

    val numerator = size * dotProduct - ratingSum * rating2Sum
    val denominator = scala.math.sqrt(size * ratingNormSq - ratingSum * ratingSum) *
      scala.math.sqrt(size * rating2NormSq - rating2Sum * rating2Sum)

    numerator / denominator
  }

  /**
    * Regularize correlation by adding virtual pseudocounts over a prior:
    * RegularizedCorrelation = w * ActualCorrelation + (1 - w) * PriorCorrelation
    * where w = # actualPairs / (# actualPairs + # virtualPairs).
    */
  def regularizedCorrelation(size: Double, dotProduct: Double, ratingSum: Double,
                             rating2Sum: Double, ratingNormSq: Double, rating2NormSq: Double,
                             virtualCount: Double, priorCorrelation: Double) = {

    val unregularizedCorrelation = correlation(size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq)
    val w = size / (size + virtualCount)

    w * unregularizedCorrelation + (1 - w) * priorCorrelation
  }

  /**
    * The cosine similarity between two vectors A, B is
    * dotProduct(A, B) / (norm(A) * norm(B))
    */
  def cosineSimilarity(dotProduct: Double, ratingNorm: Double, rating2Norm: Double) = {
    dotProduct / (ratingNorm * rating2Norm)
  }

  /**
    * The Jaccard Similarity between two sets A, B is
    * |Intersection(A, B)| / |Union(A, B)|
    */
  def jaccardSimilarity(usersInCommon: Double, totalUsers1: Double, totalUsers2: Double) = {
    val union = totalUsers1 + totalUsers2 - usersInCommon
    usersInCommon / union
  }

}



