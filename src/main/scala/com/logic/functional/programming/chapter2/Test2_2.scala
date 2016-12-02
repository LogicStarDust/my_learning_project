package com.logic.functional.programming.chapter2

/**
  * Created by logic on 2016/11/19.
  */
object Test2_2 {
  def main(args: Array[String]): Unit = {
    def oo(o:Int,t:Int):Boolean={
      true
    }
    println(isSorted(Array[Int](1, 2, 3, 4),oo))
  }

  def isSorted[A](as: Array[A], order: (A, A) => Boolean): Boolean = {
    def sort(rank: Int, ass: Array[A]): Boolean = {
      if (rank == 0) true
      else if (order(ass(rank), ass(rank - 1))) {
        sort(rank - 1, ass)
      } else false
    }
    sort(1, as)
  }

  def formatResult(name: String, n: Int, f: Int => Int): String = {
    val msg = "the %s of %d is %d"
    msg.format(name, n, f(n))
  }

  def fib(n: Int): Int = {
    @annotation.tailrec
    def go(rank: Int, numMin: Int, numMax: Int): Int = {
      if (rank <= 2) numMax
      else go(rank - 1, numMax, numMin + numMax)
    }
    go(n, 0, 1)
  }

  def factional(n: Int): Int = {
    @annotation.tailrec
    def go(n: Int, acc: Int): Int = {
      if (n <= 0) acc
      else go(n - 1, n * acc)
    }
    go(n, 1)
  }
}
















