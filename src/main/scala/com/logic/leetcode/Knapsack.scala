package com.logic.leetcode

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Knapsack {
  def main(args: Array[String]): Unit = {
    val w = Array(6, 4, 3, 1, 8, 9,12,18)
    val p = Array(7, 4, 8, 3, 1, 2,11,14)
    println(maxPack(20, w, p))
    val m = BackPackSolution(20, w, p)
    m.foreach(as => {
      as.foreach(a => print(a + "\t"))
      println()
    })
  }

  def longestPalindrome(s: String): String = {
    if (s == null || s.length == 0) {
      null
    } else if (s.length == 1) {
      s
    } else if (s.length == 2 && s.charAt(0) == s.charAt(1)) {
      s
    } else {
      val ps = longestPalindrome(s.drop(1))
      if (s.drop(1).startsWith(ps) && s.charAt(ps.length) == s.charAt(0)) s.charAt(0) + ps
      else ps
    }
  }

  /**
    *
    * @param m 表示背包的最大容量
    * @param w 表示商品重量数组
    * @param p 表示商品价值数组
    * @return
    */
  def maxPack(m: Int, w: Array[Int], p: Array[Int]): Int = {
    if (w.isEmpty || m < w(0)) 0
    else {
      val fang = maxPack(m - w(0), w.drop(1), p.drop(1)) + p(0)
      val bufang = maxPack(m, w.drop(1), p.drop(1))
      if (fang >= bufang) {
        fang
      }
      else {
        bufang
      }
    }
  }

  /**
    *
    * @param m 表示背包的最大容量
    * @param w 表示商品重量数组
    * @param p 表示商品价值数组
    */
  def BackPackSolution(m: Int, w: Array[Int], p: Array[Int]): Array[Array[Int]] = {
    val c = Array.ofDim[Int](w.length + 1, m + 1)
    //当背包容量为0，所有价值为0
    for (i <- 0 to w.length) {
      c(i)(0) = 0
    }
    //当背包商品为0，所有价值为0
    for (j <- 0 to m) {
      c(0)(j) = 0
    }
    //遍历填充矩阵
    for (i <- 1 to w.length; j <- 1 to m) {
      //当前物品重量不小于容量，当前价值则和不放当前物品的价值相等，否则进入进一步计算
      if (w(i - 1) <= j) {
        //如果不放入当前物品的价值小于放入本物品的价值，当前价值为放入本物品的价值，否则为不放入本物品的价值
        if (c(i - 1)(j) < (c(i - 1)(j - w(i - 1)) + p(i - 1))) {
          //容量j的背包装i个物品最高价值为：容量为j去掉一个商品重量，数量为i-1个商品的价值 加上 （去掉的这个商品的价值）
          c(i)(j) = c(i - 1)(j - w(i - 1)) + p(i - 1)
        } else {
          //容量j的背包装i个物品最高价值为：容量为j，数量为i-1个商品的价值
          c(i)(j) = c(i - 1)(j)
        }
      } else
        c(i)(j) = c(i - 1)(j)
    }
    c
  }
}
