package com.logic.leetcode

object TwoSum {
  def main(args: Array[String]): Unit = {
    val re = twoSum2(Array(3, 3), 6)
    print(re.mkString(","))
  }

  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    val numAndRank = nums.zip(Array.range(0, nums.length, 1))
    var num2Rank = -1
    val numRank = numAndRank.find(nr => {
      numAndRank.exists(nr2 => {
        if (nr._2 != nr2._2 && nr._1 + nr2._1 == target) {
          num2Rank = nr2._2
          true
        } else {
          false
        }
      })
    }).get._2
    Array(numRank, num2Rank)
  }

  def twoSum2(nums: Array[Int], target: Int): Array[Int] = {
    var x1, x2: Int = -1
    Array.range(0, nums.length).exists(i => {
      Array.range(i+1, nums.length).exists(j => {
//        println(s"i:$i,j:$j")
        if (nums(i) + nums(j) == target) {
          x1 = i
          x2 = j
          true
        } else {
          false
        }
      })
    })
    Array(x1, x2)
  }
}
