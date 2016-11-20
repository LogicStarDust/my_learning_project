package com.logic.scala.function

/**
  * Created by logic on 2016/11/20.
  */
object test2_3 {
  def main(args: Array[String]): Unit = {
    def add(one:Int,two:Long):BigDecimal={
      BigDecimal(one)+BigDecimal(two)
    }

    val cu=curry(add)
    val value=cu(10)
    println(value(10))
  }
  def curry[A, B, C](f: (A, B) => C): A => (B => C)=
    (a:A)=>{
      f(a,_)
    }
}
