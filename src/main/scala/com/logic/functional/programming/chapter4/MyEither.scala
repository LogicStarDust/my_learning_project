package com.logic.functional.programming.chapter4

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
sealed trait MyEither [+E,+A]{
  def map[B](f:A=>B):MyEither[E,B]=this match {
    case MyLeft(value)=>MyLeft(value)
    case MyRight(value)=>MyRight(f(value))
  }
  def flatMap[EE >: E,B](f:A=>MyEither[EE,B]):MyEither[EE,B]=this match {
    case MyLeft(value)=>MyLeft(value)
    case MyRight(value)=>f(value)
  }
  def orElse[EE >: E,B >: A](b:MyEither[EE,B]):MyEither[EE,B]=this match {
    case MyLeft(_)=>b
    case MyRight(v)=>MyRight(v)
  }
  def map2[EE >:E,B,C](b:MyEither[EE,B])(f:(A,B)=>C):MyEither[EE,C]=(this,b) match {
    case (MyLeft(v1),_)=>MyLeft(v1)
    case (_,MyLeft(v1))=>MyLeft(v1)
    case (MyRight(v1),MyRight(v2))=>MyRight(f(v1,v2))
  }
}
case class MyLeft[+E](value:E) extends MyEither[E,Nothing]
case class MyRight[+A](value:A) extends MyEither[Nothing,A]