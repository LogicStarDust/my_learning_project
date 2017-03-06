package com.logic

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Test {
  def main(args: Array[String]): Unit = {
    val add1=add(1)(_)
    val add2=add1(2)
    println(add2(4))

  }
  def add(a:Int)(b:Int)(c:Int):Int={
    a+b+c
  }
}
