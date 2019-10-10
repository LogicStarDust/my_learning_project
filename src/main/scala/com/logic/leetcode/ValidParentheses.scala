package com.logic.leetcode


object ValidParentheses {
  def main(args: Array[String]): Unit = {
    println(isValid("["))
  }

  def isValid(s: String): Boolean = {
    val p_map = Map('{' -> '}', '[' -> ']', '(' -> ')')
    val charA = s.toCharArray
    val stack = scala.collection.mutable.Stack[Char]()
    // 是否存在异常括号
    !charA.exists(c => {
//      println(s"--$c--$stack")
      if (p_map.contains(c)) {
        stack.push(c)
        false
      } else if (stack.isEmpty) {
        true
      } else {
        c != p_map(stack.pop())
      }
    }) && stack.isEmpty
  }
}
