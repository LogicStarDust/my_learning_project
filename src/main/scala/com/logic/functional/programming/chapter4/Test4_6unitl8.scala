package com.logic.functional.programming.chapter4

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Test4_6unitl8 {

  def main(args: Array[String]): Unit = {
    //4.6
    val either: MyEither[String, Int] = MyRight(1)
    println(either.map(_ + 5))

    println(either.flatMap(v => MyLeft("wang")))

    val left: MyEither[String, Int] = MyLeft("wangguodong")
    println(left.orElse(MyRight("ww")))

    val ei2 = either.map2(MyRight(4))(_ + _)
    println(ei2)
  }

  def Try[A](a: => A): MyEither[Exception, A] = {
    try MyRight(a)
    catch {
      case e: Exception => MyLeft(e)
    }
  }

  //4.7
  def sequence[E, A](es: List[MyEither[E, A]]): MyEither[E, List[A]] = {
    null
  }

}
