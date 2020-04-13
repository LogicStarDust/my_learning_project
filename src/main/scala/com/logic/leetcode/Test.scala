package com.logic.leetcode

/**
  * Created by Wang Guodong on 20/4/13.
  */
object Test {
  def main(args: Array[String]): Unit = {
    val s1 = "anagram"
    val s2 = "nagaram"
    val re = isAnagram(s1, s2)
    println(re)
  }

  def isAnagram(s: String, t: String): Boolean = {
    val c26 = new Array[Int](128)
    s.foreach(c => {
      c26(c.toInt) = c26(c.toInt) + 1
    })
    t.foreach(c => {
      c26(c.toInt) = c26(c.toInt) - 1
    })
    //    println(c26.find(_ % 2 != 0))
    !c26.exists(_ != 0)
  }
}
