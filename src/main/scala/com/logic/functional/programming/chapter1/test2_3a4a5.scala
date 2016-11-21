package com.logic.functional.programming.chapter1

/**
  * Created by logic on 2016/11/20.
  */
object test2_3a4a5 {
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
  def uncurry[A,B,C](f:A=>B=>C):(A,B)=>C=
    (a:A,b:B)=>{
      f(a)(b)
    }
  def compose[A,B,C](f:B=>C,g:A=>B):A=>C=
    (a:A)=> {
      f(g(a))
    }
}
