package com.logic.functional.programming.chapter1

import scala.reflect.ClassTag
import scala.runtime.Nothing$

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Test3_1until6 {

  def main(args: Array[String]): Unit = {

    val ml=MyList(1,2,3,4)
    val ml2=MyList(1,MyNil)
    val ml3=MyNil
    val ml4=MyList(MyNil)
    println(dropWhile(ml)((x:Int)=>x<3))
  }

  def threePointOne: Int = {
    MyList(1, 2, 3, 4, 5) match {
      case Cons(x, Cons(2, Cons(4, _))) => x
      case MyNil => 42
      case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
      case Cons(h, t) => h + MyList.sum(t)
      case _ => 101
    }
  }

  def tail[A](ml: MyList[A]): MyList[A] = ml match {
    case MyNil => MyNil
    case Cons(_, t) => t
  }

  def setHead[A](head: A)(ml: MyList[A]): MyList[A] = ml match {
    case MyNil => Cons(head, MyNil)
    case Cons(_, t) => Cons(head, t)
  }

  def drop[A](n: Int)(ml: MyList[A]): MyList[A] = n match {
    case 0 => ml
    case 1 => tail(ml)
    case x => drop(x - 1)(tail(ml))
  }

  def dropWhile[A](ml: MyList[A])(f: A => Boolean): MyList[A] = ml match {
    case MyNil => MyNil
    case Cons(h, t) if f(h) => dropWhile(t)(f)
    case x=>x
  }
  def init[A](ml:MyList[A]):A=ml match {
//    case MyNil=>??
    case Cons(h,MyNil)=>h
    case Cons(head,Cons(MyNil,MyNil))=>head
    case Cons(_,t)=>init(t)
  }
}
