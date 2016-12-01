package com.logic.functional.programming.chapter1

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */

//定义泛型的数据类型List，sealed规定所有的实现均在本文件中
sealed trait MyList[+A]
//定义一个对象MyNil是MyList的空实现
case object MyNil extends MyList[Nothing]

case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]

object MyList {
  def sum(ints: MyList[Int]): Int = ints match {
    case MyNil => 0
    case Cons(x, xs) => x + sum(xs)
  }
  def product(ds:MyList[Double]):Double=ds match {
    case MyNil=>1.0
    case Cons(0.0,_)=>0.0
    case Cons(x,xs)=>x*product(xs)
  }

  def apply[A](as:A*): MyList[A] =
    if(as.isEmpty) MyNil
    else Cons(as.head,apply(as.tail:_*))
}
-