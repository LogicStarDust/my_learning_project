package com.logic.leetcode

object RomanToInteger {
  def main(args: Array[String]): Unit = {
    println(romanToInt("IVV"))
  }

  def romanToInt(s: String): Int = {
    val spec = Map("IV" -> 4, "IX" -> 9, "XL" -> 40, "XC" -> 90, "CD" -> 400, "CM" -> 900)
    val ti = Map('I' -> 1, 'V' -> 5, 'X' -> 10, 'L' -> 50, 'C' -> 100, 'D' -> 500, 'M' -> 1000)
    var flag = false
    var sum=0
    for (i <- 0 until s.length) {
      if (flag) {
        flag = false
      } else if (i < s.length-1 && spec.contains(s(i).toString + s(i + 1).toString)) {
        flag = true
        sum=sum+spec(s(i).toString + s(i + 1).toString)
      } else {
        sum=sum+ti(s(i))
      }
    }
    sum
  }
}
