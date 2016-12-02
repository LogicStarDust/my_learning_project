package com.logic.functional.programming.chaoter2

/**
  * Created by logic on 2016/11/20.
  */
object Test2_3until5 {
  def main(args: Array[String]): Unit = {
    def add(one: Int, two: Long): BigDecimal = {
      BigDecimal(one) + BigDecimal(two)
    }

    //把add柯里化
    val g= curry(add)
    val h = g(10)
    println(h(10))

    //反柯里化
    val j=uncurry(g)
    println(j(1,2))
  }

  /**
    * 柯里化
    *
    * @param f 一个接受A类型和B类型两个参数，返回C类型的函数
    * @tparam A 泛型
    * @tparam B 泛型
    * @tparam C 泛型
    * @return 一个接受A类型，返回一个函数的函数
    */
  def curry[A, B, C](f: (A, B) => C): A => B => C =
    (a: A) => {
      (b:B)=>{
        f(a,b)
      }
    }

  /**
    * 反柯里化
    *
    * @param f 一个接受A类型参数返回函数的函数
    * @tparam A 泛型
    * @tparam B 泛型
    * @tparam C 泛型
    * @return 一个接受A类型和B类型两个参数，返回C类型的函数
    */
  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    (a: A, b: B) => {
      f(a)(b)
    }

  /**
    * 合并两个函数
    *
    * @param f 接受B类型，返回C类型函数
    * @param g 接受A类型，返回B类型函数
    * @tparam A 泛型
    * @tparam B 泛型
    * @tparam C 泛型
    * @return 接受A类型，返回C类型的函数
    */
  def compose[A, B, C](f: B => C, g: A => B): A => C =
    (a: A) => {
      f(g(a))
    }
}
