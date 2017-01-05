package com.logic.functional.programming.chapter4


/**
  * Created by logic on 2016/12/24.
  */
object Test4_1unitl5 {
  def main(args: Array[String]): Unit = {
    //4.1
    val list: List[MyOption[String]] =
      List(
        MySome("a"), MySome("b"), MyNone,
        MySome("d"), MySome("e"), MyNone
      )
    val oo: MyOption[MyOption[Int]] = MySome(MySome(23))
    val n: MyOption[Int] = MyNone
    val o: MyOption[Int] = MySome(6)
    println(list.map(_.map(_ + "1")))
    println(oo.flatMap(x => x))
    println(n.getOrElse(11))
    println(n.orElse(MySome("aa")))
    println(o.filter(_ > 32))
    //4.2
    val s = Seq(1.1, 2.3, 3.2, 4.3, 5.4)
    println(variance(s) + "," + variance(Seq()))

    //4.4
    val list1 = List(MySome(1), MySome(2), MyNone)
    println(sequence(list1))

    val list2 = List(Some(1), Some(2), None)
    println(list2.flatten)
    //4.5
    val list3 = List("1", "2", "s")
    val tralist = traverse(list3)(x => MySome(x.toInt))
    println(tralist)
    Left
  }


  //4.2
  def mean(xs: Seq[Double]): MyOption[Double] = {
    if (xs.isEmpty) MyNone
    else MySome(xs.sum / xs.size)
  }

  def variance(xs: Seq[Double]): MyOption[Double] = {
    mean(xs).flatMap(
      m => mean(xs.map(x => math.pow(x - m, 2)))
    )
  }

  //4.3
  def map2[A, B, C](a: MyOption[A], b: MyOption[B])(f: (A, B) => C): MyOption[C] = (a, b) match {
    case (MySome(av), MySome(bv)) => MySome(f(av, bv))
    case _ => MyNone
  }

  //4.4
  def sequence[A](a: List[MyOption[A]]): MyOption[List[A]] = {
    Try(a.map(x => x.getOrElse(throw new Exception)))
  }

  //4.5
  def traverse[A, B](a: List[A])(f: A => MyOption[B]): MyOption[List[B]] = {
    Try(a.map(x => f(x).getOrElse(throw new Exception)))
  }

  def Try[A](a: => A): MyOption[A] = {
    try MySome(a)
    catch {
      case _: Exception => MyNone
    }
  }
}
